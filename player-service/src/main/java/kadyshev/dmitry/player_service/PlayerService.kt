package kadyshev.dmitry.player_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kadyshev.dmitry.core_player.MusicPlayerManager
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.inject

class PlayerService : Service() {

    private val binder = PlayerBinder()

    private val musicPlayerManager: MusicPlayerManager by inject()

    private var playerData: PlayerData? = null
    private var currentIndex: Int = 0

    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    var listener: PlayerListener? = null


    inner class PlayerBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        musicPlayerManager.onProgressChanged = { cur, tot ->
            listener?.onProgressChanged(cur, tot)
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { i ->
            // 1) Если пришли данные первого запуска — стартуем плей
            i.getStringExtra("playerData")?.let { json ->
                val data = Json.decodeFromString<PlayerData>(json)
                val index = i.getIntExtra("startIndex", 0)
                start(data, index)
            }
            // 2) Всегда обрабатываем action из уведомления
            when (i.action) {
                ACTION_TOGGLE -> togglePlayPause()
                ACTION_NEXT   -> moveToNext()
                ACTION_PREV   -> moveToPrev()
            }
        }

        // Немедленно переводим сервис в фоновый режим, вызвав startForeground
        playerData?.tracks?.get(currentIndex)?.let { track ->
            startForeground(NOTIF_ID, createNotification(track))
        }

        return START_STICKY
    }



    fun start(playerData: PlayerData, startIndex: Int) {
        this.playerData = playerData
        this.currentIndex = startIndex
        playCurrent()
    }

    private fun playCurrent() {
        val track = playerData?.tracks?.get(currentIndex) ?: return

        // говорим, что трек поменялся
        listener?.onTrackChanged(track, currentIndex)

        musicPlayerManager.play(track.getPlayablePath()) {
            moveToNext()
        }
        listener?.onPlayStateChanged(true)
        startForeground(NOTIF_ID, createNotification(track))
    }

    fun togglePlayPause() {
        if (musicPlayerManager.isPlaying()) {
            musicPlayerManager.pause()
            listener?.onPlayStateChanged(false)
        } else {
            musicPlayerManager.resume()
            listener?.onPlayStateChanged(true)
        }
        updateNotification()
    }

    fun moveToNext() {
        if (currentIndex + 1 < (playerData?.tracks?.size ?: 0)) {
            currentIndex++
            playCurrent()   // вызовет onTrackChanged + onPlayStateChanged(true)
        }
    }

    fun moveToPrev() {
        if (currentIndex - 1 >= 0) {
            currentIndex--
            playCurrent()
        }
    }

    private fun createNotification(track: Track): Notification {
        // Intents на внешний NotificationReceiver
        val playPauseIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = ACTION_TOGGLE
        }
        val nextIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = ACTION_NEXT
        }
        val prevIntent = Intent(this, NotificationReceiver::class.java).apply {
            action = ACTION_PREV
        }

        val pendingPlayPause = PendingIntent.getBroadcast(
            this, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pendingNext = PendingIntent.getBroadcast(
            this, 1, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val pendingPrev = PendingIntent.getBroadcast(
            this, 2, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(track.title)
            .setContentText(track.artist)
            .setSmallIcon(R.drawable.ic_note)
            .addAction(R.drawable.ic_previous, "Prev", pendingPrev)
            .addAction(
                if (musicPlayerManager.isPlaying()) R.drawable.ic_pause else R.drawable.ic_play,
                "Play/Pause",
                pendingPlayPause
            )
            .addAction(R.drawable.ic_next, "Next", pendingNext)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle())
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }


    private fun updateNotification() {
        playerData?.tracks?.get(currentIndex)?.let {
            notificationManager.notify(NOTIF_ID, createNotification(it))
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun Track.getPlayablePath(): String {
        return localPath ?: previewUrl
    }

    companion object {
        private const val CHANNEL_ID = "music_channel"
        private const val NOTIF_ID = 1

        const val ACTION_TOGGLE = "ACTION_TOGGLE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREV = "ACTION_PREV"
    }
}
