package net.nickchen450.player

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.util.*

/**
 * 封裝ExoPlayer View
 * */
class UIPlayerController : PlayerView, IPlayerController {

    private lateinit var url: String
    private var player: SimpleExoPlayer? = null
    private var playerListener: IPlayerListener? = null
    private var trackTimer: Timer? = null
    private var currentPosition: Long = 0L

    constructor(context: Context?) : super(context) {
        setUpDefaultPlayerConfig()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setUpDefaultPlayerConfig()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setUpDefaultPlayerConfig()
    }

    override fun setMediaResourceUrl(url: String, autoPlayWhenReady: Boolean) {
        this.url = url
        release()
        player = buildMediaSource(url)
        player?.playWhenReady = autoPlayWhenReady
        setPlayer(player)
    }

    override fun play() {
        seekToPosition(0L)
        player?.playWhenReady = true
    }

    override fun pause() {
        player?.playWhenReady = false
    }

    override fun resume() {
        seekToPosition(currentPosition)
        player?.playWhenReady = true
    }

    override fun stop() {
        stopTrackingTimer()
        player?.stop()
    }

    override fun seekToPosition(position: Long) {
        player?.seekTo(position)
    }

    override fun release() {
        stop()
        player?.release()
    }

    override fun isPlaying(): Boolean {
        return if (player == null)
            false
        else
            player!!.playWhenReady && player!!.playbackState == Player.STATE_READY
    }

    override fun getEntireTime(): Long {
        return if (player == null)
            0L
        else
            player!!.duration
    }

    override fun getCurrentPointTime(): Long {
        return if (player == null)
            0L
        else
            player!!.currentPosition
    }

    override fun setPlayerListener(listener: IPlayerListener) {
        this.playerListener = listener;
    }

    // 初始化
    private fun setUpDefaultPlayerConfig() {
        this.useController = false
        this.keepScreenOn = true
        //this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    // 建立播放器
    private fun buildMediaSource(url: String): SimpleExoPlayer {
        // Preparing the player

        // Produces DataSource instances through which media data is loaded.
        val factory: DataSource.Factory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, context.getString(R.string.app_name)),
            null
        )

        // This is the MediaSource representing the media to be played.
        val videoSource = ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(url))
        //
        val simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context)
        simpleExoPlayer.prepare(videoSource)
        simpleExoPlayer.addListener(PlayListener())
        return simpleExoPlayer
    }

    // 將當前播放進度(時間)，pass給監聽者
    private fun startTrackingTimer() {
        stopTrackingTimer()
        trackTimer = Timer(true)
        trackTimer?.schedule(object : TimerTask() {
            override fun run() {
                rootView.post {
                    playerListener?.playing(getCurrentPointTime())
                }
            }
        }, 0, 1000)
    }

    private fun stopTrackingTimer() {
        trackTimer?.cancel()
    }

    private inner class PlayListener : Player.EventListener {

        override fun onPlayerError(error: ExoPlaybackException?) {
            super.onPlayerError(error)
            stopTrackingTimer()
            playerListener?.fail(error?.message)
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)

            currentPosition = getCurrentPointTime()

            //播放中
            if (playWhenReady && playbackState == Player.STATE_READY) {
                playerListener?.ready()

                if (isPlaying())
                    startTrackingTimer()
                else
                    stopTrackingTimer()
            }

            // 緩衝/暫停中
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    playerListener?.buffering()
                }

                Player.STATE_IDLE -> {
                    stopTrackingTimer()
                    playerListener?.idel()
                }

                Player.STATE_ENDED -> {
                    currentPosition = 0L
                    stopTrackingTimer()
                    playerListener?.end()
                }
            }
        }
    }
}