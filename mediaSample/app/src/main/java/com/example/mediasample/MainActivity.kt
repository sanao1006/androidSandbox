package com.example.mediasample

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var _player: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _player = MediaPlayer()
        val mediaFileUriStr = "android.resource://${packageName}/${R.raw.sound_environment}"
        val mediaFileUri = Uri.parse(mediaFileUriStr)

        _player?.let{
            it.setDataSource(this@MainActivity,mediaFileUri)
            it.setOnPreparedListener(PlayerPreparedListener())
            it.prepareAsync()
        }
    }

    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer?) {
            TODO("Not yet implemented")
        }

    }
}