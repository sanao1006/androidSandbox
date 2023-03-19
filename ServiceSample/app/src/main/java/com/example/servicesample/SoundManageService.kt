package com.example.servicesample

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class SoundManageService : Service() {
    companion object {
        private const val CHANNEL_ID = "soundmanagerservice_notification_channel"
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    private var _player: MediaPlayer? = null
    override fun onCreate() {
        _player = MediaPlayer()
        val name = getString(R.string.notification_channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =NotificationChannel(CHANNEL_ID, name, importance)
//      NotificationManagerオブジェクトを取得
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val mediaFileUrlStr = "android.resource://${packageName}/${R.raw.sound_environment}"
        val mediaFileUrl = Uri.parse(mediaFileUrlStr)
        _player?.let {
//          メディアプレーヤーに音声ファイルを指定
            it.setDataSource(this@SoundManageService, mediaFileUrl)
            it.setOnPreparedListener(PlayerPreparedListener())
            it.setOnCompletionListener(PlayerCompletionListener())
            it.prepareAsync()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        _player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
    }

    private inner class PlayerCompletionListener : MediaPlayer.OnCompletionListener {
        override fun onCompletion(mp: MediaPlayer) {
//          Notification を作成するBuilderクラスの生成
            val builder = NotificationCompat.Builder(this@SoundManageService, CHANNEL_ID)
//          通知エリアに表示されるアイコン設定
            builder.setSmallIcon(android.R.drawable.ic_dialog_info)
            builder.setContentTitle(getString(R.string.msg_notification_title_finish))
            builder.setContentText(getString(R.string.msg_notification_text_finish))

            val notification = builder.build()
            val manager = NotificationManagerCompat.from(this@SoundManageService)
            manager.notify(100, notification)
            stopSelf()
        }

    }

    private inner class PlayerPreparedListener : MediaPlayer.OnPreparedListener {
        override fun onPrepared(mp: MediaPlayer) {
            mp.start()
        }

    }
}