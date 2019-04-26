package net.nickchen450.exoplayersample.cutsomize

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_custom.*
import net.nickchen450.exoplayersample.R
import java.util.*


class CustomActivity : AppCompatActivity() {

    companion object {
        private const val URL_MP3 = "https://audionautix.com/Music/Limosine.mp3"
    }

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)

        ui_media_view.setMediaResourceUrl(URL_MP3)

        btn_play.setOnClickListener {
            if (ui_media_view.isPlaying()) {
                ui_media_view.stop()
                btn_play.text = "PLAY"
            } else {
                ui_media_view.play()
                btn_play.text = "PAUSE"
            }
        }

        ui_media_view.setOnSeekBarListener { it: Long ->

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                seek_bar.setProgress(it.toInt(), false)
            } else{
                seek_bar.progress = it.toInt()
            }
        }

        seek_bar.max = ui_media_view.getTotalTime().toInt()

        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.apply {
                    ui_media_view.seekToTime(this.toLong())
                }
            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }
        })

        startTimer()

    }

    private fun startTimer() {
        stopTimer()
        timer = Timer(true)
        timer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    seek_bar.max = ui_media_view.getTotalTime().toInt()
                    seek_bar.progress = ui_media_view.getCurrentTime().toInt()
                    txt_duration.text = convertTimeToMinutes(ui_media_view.getCurrentTime())
                    txt_total_time.text = convertTimeToMinutes(ui_media_view.getTotalTime())
                }
            }
        }, 1000, 1000)
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    private fun convertTimeToMinutes(durationInMillis: Long): String {
        val millis = durationInMillis % 1000
        val second = durationInMillis / 1000 % 60
        val minute = durationInMillis / (1000 * 60) % 60
        val hour = durationInMillis / (1000 * 60 * 60) % 24
        return String.format("%02d:%02d:%02d", hour, minute, second)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        ui_media_view.stop()
    }
}
