package net.nickchen450.exoplayersample

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_exoplayer.*

/**
 * 使用原生 ExoPlayer & Controller bar
 * */
class ExoPlayerActivity : AppCompatActivity() {

    companion object {
        private const val URL_MP4 = "http://demos.webmproject.org/exoplayer/glass.mp4"
        private const val URL_MP3 = "https://audionautix.com/Music/Limosine.mp3"
    }

    private lateinit var player: SimpleExoPlayer
    private var mResumePosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exoplayer)
        player = buildMediaSource()
        player.playWhenReady = true
        player.addListener(PlayListener())

        player_view.player = player
    }

    override fun onResume() {
        super.onResume()
        resumeExoPlayer()
    }

    override fun onPause() {
        super.onPause()
        pauseExoPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseExoPlayer()
    }

    private fun pauseExoPlayer() {
        player.playWhenReady = false
        mResumePosition = player.currentPosition
    }

    private fun releaseExoPlayer() {
        player.release()
    }

    private fun resumeExoPlayer() {
        player.seekTo(mResumePosition)
    }

    private fun buildMediaSource(): SimpleExoPlayer {
        // Preparing the player

        // Produces DataSource instances through which media data is loaded.
        val factory: DataSource.Factory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"))
        // This is the MediaSource representing the media to be played.
        val videoSource =
            ExtractorMediaSource.Factory(factory).createMediaSource(Uri.parse(URL_MP3))
        //
        val simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this)
        simpleExoPlayer.prepare(videoSource)
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
            when (playbackState) {
                Player.STATE_BUFFERING -> {
                    video_progressbar.visibility = View.VISIBLE
                }
                Player.STATE_READY -> {
                    video_progressbar.visibility = View.INVISIBLE
                }
            }
        }
    }

}
