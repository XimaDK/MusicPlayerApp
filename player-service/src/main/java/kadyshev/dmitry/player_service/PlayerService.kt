package kadyshev.dmitry.player_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import kadyshev.dmitry.core_player.MusicPlayerManager
import kadyshev.dmitry.domain.entities.PlayerData
import kadyshev.dmitry.domain.entities.Track
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.koin.android.ext.android.inject

class PlayerService : Service() {

    private val binder = PlayerBinder()

    private val musicPlayerManager: MusicPlayerManager by inject()

    private var playerData: PlayerData? = null
    private var currentIndex: Int = 0

    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    var onPlayerReady: ((duration: Int) -> Unit)? = null

    private var listener: PlayerListener? = null


    inner class PlayerBinder : Binder() {
        fun getService(): PlayerService = this@PlayerService
    }

    fun updateListener(listener: PlayerListener?) {
        this.listener = listener
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        musicPlayerManager.onProgressChanged = { cur, tot ->
            listener?.onProgressChanged(cur, tot)
        }

        musicPlayerManager.onPlayerReady = { duration ->
            onPlayerReady?.invoke(duration)

        }

    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_TOGGLE -> togglePlayPause()
            ACTION_NEXT -> moveToNext()
            ACTION_PREV -> moveToPrev()
            ACTION_REWIND_10 -> rewindBy10Seconds()
            ACTION_FORWARD_10 -> forwardBy10Seconds()
        }
        return START_STICKY
    }


    fun notifyCurrentState() {
        val track = playerData?.tracks?.getOrNull(currentIndex)
        Log.d("PlayerService", track.toString())

        if (track != null) {
            listener?.onTrackChanged(track, currentIndex)
        }

        listener?.onProgressChanged(
            musicPlayerManager.currentPosition,
            musicPlayerManager.getDuration()
        )

        listener?.onPlayStateChanged(musicPlayerManager.isPlaying())
    }


    fun start(playerData: PlayerData, startIndex: Int) {
        this.playerData = playerData
        currentIndex = startIndex
        playCurrent()
    }

    private fun playCurrent() {
        val track = playerData?.tracks?.get(currentIndex) ?: return

        listener?.onTrackChanged(track, currentIndex)

        startForeground(NOTIF_ID, createNotification(track, true))

        musicPlayerManager.play(track.getPlayablePath()){
            moveToNext()
        }
        listener?.onPlayStateChanged(true)
    }

    fun togglePlayPause() {
        val willPlay = !musicPlayerManager.isPlaying()

        updateNotificationState(willPlay)

        if (willPlay) {
            musicPlayerManager.resume()
        } else {
            musicPlayerManager.pause()
        }

        listener?.onPlayStateChanged(willPlay)

    }

    private fun rewindBy10Seconds() {
        val currentPosition = musicPlayerManager.currentPosition
        val newPosition = (currentPosition - 10000).coerceAtLeast(0)
        musicPlayerManager.seekTo(newPosition)
        updateNotificationState(musicPlayerManager.isPlaying())
    }

    private fun forwardBy10Seconds() {
        val currentPosition = musicPlayerManager.currentPosition
        val newPosition = (currentPosition + 10000).coerceAtMost(musicPlayerManager.getDuration())
        musicPlayerManager.seekTo(newPosition)
        updateNotificationState(musicPlayerManager.isPlaying())
    }

    private fun updateNotificationState(isPlaying: Boolean) {
        playerData?.tracks?.get(currentIndex)?.let { track ->
            Log.d("PlayerService", "Updating notification for track: ${track.title}")

            notificationManager.notify(NOTIF_ID, createNotification(track, isPlaying))
        }
    }

    fun moveToNext() {
        Log.d("PlayerService", "moveToNext() called")
        if (currentIndex + 1 < (playerData?.tracks?.size ?: 0)) {
            currentIndex++
            playCurrent()
        }
    }

    fun moveToPrev() {
        if (currentIndex - 1 >= 0) {
            currentIndex--
            playCurrent()
        }
    }

    fun seekTo(position: Int) {
        musicPlayerManager.seekTo(position)
        playerData?.tracks?.get(currentIndex)?.let { track ->
            notificationManager.notify(
                NOTIF_ID,
                createNotification(track, musicPlayerManager.isPlaying())
            )
        }
    }

    fun getCurrentDuration(): Int {
        return musicPlayerManager.getDuration()
    }


    private fun createNotification(track: Track, isPlaying: Boolean? = null): Notification {
        val actualIsPlaying = isPlaying ?: musicPlayerManager.isPlaying()
        val remoteViews = RemoteViews(this.packageName, R.layout.notification_player)

        remoteViews.apply {
            setTextViewText(R.id.notification_title, track.title)
            setTextViewText(R.id.notification_artist, track.artist)

            setImageViewResource(
                R.id.notification_play_pause,
                if (actualIsPlaying) R.drawable.ic_pause else R.drawable.ic_play
            )

            // Добавляем обработчики для новых кнопок перемотки
            setOnClickPendingIntent(R.id.notification_prev, createPendingIntent(ACTION_PREV))
            setOnClickPendingIntent(
                R.id.notification_play_pause,
                createPendingIntent(ACTION_TOGGLE)
            )
            setOnClickPendingIntent(R.id.notification_next, createPendingIntent(ACTION_NEXT))
            setOnClickPendingIntent(
                R.id.notification_rewind_10,
                createPendingIntent(ACTION_REWIND_10)
            )
            setOnClickPendingIntent(
                R.id.notification_forward_10,
                createPendingIntent(ACTION_FORWARD_10)
            )

        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_note)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(remoteViews)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }


    private fun createPendingIntent(action: String): PendingIntent {
        val intent = Intent(this, NotificationReceiver::class.java).apply {
            this.action = action
        }
        return PendingIntent.getBroadcast(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
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

        private const val ACTION_REWIND_10 = "ACTION_REWIND_10"
        private const val ACTION_FORWARD_10 = "ACTION_FORWARD_10"
        const val ACTION_TOGGLE = "ACTION_TOGGLE"
        const val ACTION_NEXT = "ACTION_NEXT"
        const val ACTION_PREV = "ACTION_PREV"
    }
}
