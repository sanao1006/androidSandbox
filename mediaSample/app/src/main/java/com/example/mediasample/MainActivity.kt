package com.example.mediasample

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {
    private var _player: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _player = MediaPlayer()
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.sound_environment}"
        val mediaFileUri = Uri.parse(mediaFileUriStr)

        _player?.let {
            it.setDataSource(this@MainActivity, mediaFileUri)
            it.setOnPreparedListener(PlayerPreparedListener())
            it.setOnCompletionListener(PlayerCompletionListener())
            it.prepareAsync()
        }

        val loopSwitch = findViewById<SwitchMaterial>(R.id.swLoop)
        loopSwitch.setOnCheckedChangeListener(LoopSwitchChangedListener())
    }


    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer) {
            val btPlay = findViewById<Button>(R.id.btPlay)
            btPlay.isEnabled = true
            val btBack = findViewById<Button>(R.id.btBack)
            btBack.isEnabled = true
            val btForward = findViewById<Button>(R.id.btForward)
            btForward.isEnabled = true
        }

    }

    private inner class PlayerCompletionListener : MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer?) {
            _player?.let {
                if (!it.isLooping) {
//            再生ボタンを押したときのラベルを「再生」にする
                    val btPlay = findViewById<Button>(R.id.btPlay)
                    btPlay.setText(R.string.bt_play_play)
                }
            }
        }

    }


    fun onPlayButtonClick(view: View) {
        _player?.let {
            val btPlay = findViewById<Button>(R.id.btPlay)
            if (it.isPlaying) {
//              一時停止
                it.pause()
                btPlay.setText(R.string.bt_play_play)
            } else {
//              再生開始
                it.start()
                btPlay.setText(R.string.bt_play_pause)
            }
        }
    }

    override fun onStop() {
        _player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        super.onStop()
    }

    fun onBackButton(view: View) {
        _player?.seekTo(0)
    }

    fun onForwardButton(view: View) {
        _player?.let {
//          再生中のメディアの長さを取得
            val duration = it.duration
            it.seekTo(duration)
            if (it.isPlaying) {
                val btPlay = findViewById<Button>(R.id.btPlay)
                btPlay.setText(R.string.bt_play_pause)
                it.start()
            }
        }
    }

    private inner class LoopSwitchChangedListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            _player?.isLooping = isChecked
        }

    }

}