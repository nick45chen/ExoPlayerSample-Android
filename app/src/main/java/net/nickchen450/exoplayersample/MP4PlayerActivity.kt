package net.nickchen450.exoplayersample

import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import kotlinx.android.synthetic.main.activity_mp4_player.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util


class MP4PlayerActivity : AppCompatActivity() {

    companion object {
        const val URL = "http://demos.webmproject.org/exoplayer/glass.mp4"
    }

    private lateinit var player: SimpleExoPlayer
    private val bandwidthMeter by lazy {
        DefaultBandwidthMeter()
    }

    private val adaptiveTrackSelectionFactory by lazy {
        AdaptiveTrackSelection.Factory(bandwidthMeter)
    }
    private var mResumePosition = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mp4_player)

        //player = ExoPlayerFactory.newSimpleInstance(this)
        player = ExoPlayerFactory.newSimpleInstance(
            this,
            DefaultRenderersFactory(this),
            DefaultTrackSelector(adaptiveTrackSelectionFactory),
            DefaultLoadControl()
        )
        player_view.player = player

        player.seekToDefaultPosition()
        player.prepare(buildMediaSourceMP4(this, Uri.parse(URL)))
        player.playWhenReady = true
        player.addListener(PlayListener())
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

    private fun buildMediaSourceMP4(context: Context, uri: Uri): MediaSource {
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "ExoPlayerSample"),
            bandwidthMeter
        )
        return ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
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
