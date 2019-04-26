package net.nickchen450.exoplayersample.cutsomize

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class UIMediaView : PlayerView {

    private lateinit var player: SimpleExoPlayer
    private var onSeekUpdate: ((Long) -> Unit)? = null

    constructor(context: Context?) : super(context) {
        initConfig()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initConfig()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initConfig()
    }

    // 初始化
    private fun initConfig() {
        this.useController = false
        this.keepScreenOn = true
        //this.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    public fun setMediaResourceUrl(url: String) {
        player = buildMediaSource(url)
        player.playWhenReady = false
        setPlayer(player)
    }

    public fun play() {
        player.playWhenReady = true
    }

    public fun stop() {
        player.playWhenReady = false
    }

    public fun seekToTime(time: Long) {
        player.seekTo(time)
    }

    public fun seekWithPercent(progress: Int) {
        player.seekTo(getCurrentPositionWithPercent(progress))
    }

    public fun release() {
        stop()
        player.release()
    }

    public fun getTotalTime(): Long {
        return player.duration
    }

    public fun getCurrentTime(): Long {
        return player.currentPosition
    }

    public fun isPlaying(): Boolean {
        return player.playWhenReady && player.playbackState == Player.STATE_READY
    }

    public fun setOnSeekBarListener(listener: ((Long) -> Unit)) {
        this.onSeekUpdate = listener
    }

    private fun getCurrentPositionWithPercent(percent: Int): Long {
        return getTotalTime() * percent / 100L
    }


    private fun buildMediaSource(url: String): SimpleExoPlayer {
        // Preparing the player

        // Produces DataSource instances through which media data is loaded.
        val factory: DataSource.Factory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "yourApplicationName"),
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

    inner class PlayListener : Player.EventListener {

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) =
            super.onPlaybackParametersChanged(playbackParameters)

        override fun onSeekProcessed() {
            super.onSeekProcessed()
        }

        override fun onTracksChanged(
            trackGroups: TrackGroupArray?,
            trackSelections: TrackSelectionArray?
        ) {
            super.onTracksChanged(trackGroups, trackSelections)
        }

        override fun onPlayerError(error: ExoPlaybackException?) {
            super.onPlayerError(error)
        }

        override fun onLoadingChanged(isLoading: Boolean) {
            super.onLoadingChanged(isLoading)
        }

        override fun onPositionDiscontinuity(reason: Int) {
            super.onPositionDiscontinuity(reason)
        }

        override fun onRepeatModeChanged(repeatMode: Int) {
            super.onRepeatModeChanged(repeatMode)
        }

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            super.onShuffleModeEnabledChanged(shuffleModeEnabled)
        }

        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
            super.onTimelineChanged(timeline, manifest, reason)
        }

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            super.onPlayerStateChanged(playWhenReady, playbackState)

            onSeekUpdate?.invoke(getCurrentTime())

            //播放中
            if (playWhenReady && playbackState == Player.STATE_READY) {
                //video_progressbar.visibility = View.INVISIBLE
            }

            // 緩衝/暫停中
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    //video_progressbar.visibility = View.VISIBLE
                }

                Player.STATE_IDLE -> {
                }

                Player.STATE_ENDED -> {
                }
            }
        }
    }
}