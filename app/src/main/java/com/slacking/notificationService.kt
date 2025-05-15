package com.slacking
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock.sleep
import androidx.core.app.NotificationCompat

class notificationService : Service() {
    private fun run_(){
        val id = 10
        val notification_ = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val ChannelID = "$id"
        val builder = NotificationCompat.Builder(this, ChannelID)
        val intent = Intent(applicationContext, notificationAction::class.java).apply {
            putExtra("MESSAGE", "Clicked!")
        }
        val notificationChannel = NotificationChannel(ChannelID,
            "Slacking",
            NotificationManager.IMPORTANCE_DEFAULT);
        notification_.createNotificationChannel(notificationChannel)
        builder.setSmallIcon(R.drawable.ic_notification_name)
            .setContentTitle("Slacking")
            .setContentText("Select option for activate gaming mode")
            .addAction(0, "Apply", PendingIntent.getBroadcast(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE))
            .setOngoing(true)
            .setAutoCancel(false)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setSilent(true)

        notification_.notify(id, builder.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { onTaskRemoved(it) }
        run_()
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        Thread{
            sleep(5000)
            val restartServiceIntent = Intent(applicationContext, this.javaClass)
            restartServiceIntent.setPackage(packageName)
            startService(restartServiceIntent)
            super.onTaskRemoved(rootIntent)
        }.start()
    }
}