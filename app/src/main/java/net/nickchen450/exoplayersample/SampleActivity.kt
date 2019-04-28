package net.nickchen450.exoplayersample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sample.*
import net.nickchen450.player.IPlayerListener

/**
 * 使用封裝後的Player庫(ExoPlayer)
 * */
class SampleActivity : AppCompatActivity() {

    companion object {
        private const val URL_MP3 = "https://audionautix.com/Music/Limosine.mp3"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        ui_media_controller_view.setMediaResourceUrl(URL_MP3, false)
        ui_media_controller_view.setPlayerListener(object : IPlayerListener {
            override fun buffering() {
                progressbar.visibility = View.VISIBLE
            }

            override fun idel() {

            }

            override fun end() {
                progressbar.visibility = View.GONE
                seek_bar.progress = 0
                txt_duration.text = convertTimeToMinutes(0)
                btn_play.text = "PLAY"
            }

            override fun playing(position: Long) {
                seek_bar.progress = ui_media_controller_view.getCurrentPointTime().toInt()
                txt_duration.text = convertTimeToMinutes(ui_media_controller_view.getCurrentPointTime())
                txt_total_time.text = convertTimeToMinutes(ui_media_controller_view.getEntireTime())
            }

            override fun ready() {
                progressbar.visibility = View.GONE
                seek_bar.max = ui_media_controller_view.getEntireTime().toInt()
                txt_duration.text =
                    convertTimeToMinutes(ui_media_controller_view.getCurrentPointTime())
                txt_total_time.text = convertTimeToMinutes(ui_media_controller_view.getEntireTime())
            }

            override fun fail(message: String?) {
                Toast.makeText(this@SampleActivity, message, Toast.LENGTH_SHORT).show()
            }
        })

        // 監聽播放按鈕
        btn_play.setOnClickListener {
            if (ui_media_controller_view.isPlaying()) {
                ui_media_controller_view.pause()
                btn_play.text = "PLAY"
            } else {
                ui_media_controller_view.resume()
                btn_play.text = "PAUSE"
            }
        }

        // 監聽 Seekbar 事件
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.progress?.toLong()?.apply {
                    ui_media_controller_view.seekToPosition(this)
                }
            }
        })

        txt_duration.text = convertTimeToMinutes(0L)
        txt_total_time.text = convertTimeToMinutes(0L)
    }

    private fun convertTimeToMinutes(durationInMillis: Long): String {
        val millis = durationInMillis % 1000
        val second = durationInMillis / 1000 % 60
        val minute = durationInMillis / (1000 * 60) % 60
        val hour = durationInMillis / (1000 * 60 * 60) % 24
        return String.format("%02d:%02d:%02d", hour, minute, second)
    }
}