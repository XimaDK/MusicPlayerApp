package kadyshev.dmitry.player_service

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.core.content.ContextCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val serviceIntent = Intent(context, PlayerService::class.java).apply {
            this.action = action
        }
        ContextCompat.startForegroundService(context, serviceIntent)
    }
}

