package com.brunoponte.everythinglisboa.ui.counterService

import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Icon
import android.media.session.MediaSession
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.brunoponte.everythinglisboa.R
import com.brunoponte.everythinglisboa.helpers.Util.Companion.getBitmapFromDrawable
import com.brunoponte.everythinglisboa.ui.MainActivity

private const val NOTIFICATION_ID = 100

class CounterService : Service() {

    private var _notificationBuilder: Notification.Builder? = null
    private var _notificationBuilderCompat: NotificationCompat.Builder? = null

    private var _notificationManager: NotificationManager? = null
    private var _notificationManagerCompat: NotificationManagerCompat? = null

    private var _isPlaying = false

    override fun onCreate() {
        super.onCreate()
    }

    // When startService() is called
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) : Int {
        if (intent == null) {
            stopSelf()
        }

        initializeNotificationManagers()

        when (intent?.action) {
            PLAY_ACTION_CODE -> playPause()
            BACK_ACTION_CODE -> back()
            FORWARD_ACTION_CODE -> forward()
            START_SERVICE_ACTION_CODE -> startForeground(NOTIFICATION_ID, createNotification())
            else -> stopSelf()
        }

        return START_STICKY // tells the OS to recreate the service when it has enough memory
        //return START_NOT_STICKY // tells the OS to not bother recreating the service again
        //return START_REDELIVER_INTENT // tells the OS to recreate the service and redeliver the same intent
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    // When stopSelf() or stopService() is called
    override fun onDestroy() {
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Foreground Service Channel",
            NotificationManager.IMPORTANCE_LOW
        )
        serviceChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        val manager = getSystemService(
            NotificationManager::class.java
        )
        manager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification() : Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            createNotificationChannel()

            _notificationBuilder = Notification.Builder(this, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setTicker("Everything Android. Service is running.") // Accessibility
                .setContentTitle("Simple Foreground Service")
                .setContentText("Explain about the service")
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                // Apply the media style template
                .setStyle(
                    Notification.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(MediaSession(this, "TAG").sessionToken))
                .setOnlyAlertOnce(true)
                .setLargeIcon(
                    Bitmap.createScaledBitmap(
                        getBitmapFromDrawable(this, R.drawable.ic_launcher_foreground),
                        128,
                        128,
                        false))

            _notificationBuilder!!.build()
        } else {
            _notificationBuilderCompat = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setTicker("Everything Android. Service is running.") // Accessibility
                .setContentTitle("Simple Foreground Service")
                .setContentText("Explain about the service")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOnlyAlertOnce(true)
                .setLargeIcon(
                    Bitmap.createScaledBitmap(
                        getBitmapFromDrawable(this, R.drawable.ic_launcher_foreground),
                        128,
                        128,
                        false))
                .setColor(Color.argb(1, 1, 0, 1))

            _notificationBuilderCompat!!.build()
        }

        updateActions()

        return notification
    }

    private fun playPause() {
        Log.d(CounterService::class.java.simpleName, "Play")
        // Do logic to play/pause the music
        _isPlaying = !_isPlaying
        updateActions()
    }

    private fun back() {
        Log.d(CounterService::class.java.simpleName, "Back")
        // Do logic to move music back

    }

    private fun forward() {
        Log.d(CounterService::class.java.simpleName, "Forward")
        // Do logic to move music forward

    }

    private fun updateActions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _notificationBuilder!!.setActions()

            actions.forEach {
                val action = it as Notification.Action
                _notificationBuilder!!.addAction(action)
            }

            _notificationManager!!.notify(NOTIFICATION_ID, _notificationBuilder!!.build())
        } else {
            _notificationBuilderCompat!!.clearActions()

            actions.forEach {
                val action = it as NotificationCompat.Action
                _notificationBuilderCompat!!.addAction(action)
            }

            _notificationManagerCompat!!.notify(NOTIFICATION_ID, _notificationBuilderCompat!!.build())
        }
    }

    // Setters and Getters

    private fun initializeNotificationManagers() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            _notificationManager = getSystemService(NotificationManager::class.java)
        } else {
            _notificationManagerCompat = NotificationManagerCompat.from(this)
        }
    }

    private val actionIntents: List<PendingIntent>
    get() {
        val backActionIntent = Intent(this, CounterService::class.java).apply {
            action = BACK_ACTION_CODE
        }

        val backPendingIntent = PendingIntent.getService(
            this,
            BACK_REQUEST_CODE,
            backActionIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)

        val playActionIntent = Intent(this, CounterService::class.java).apply {
            action = PLAY_ACTION_CODE
        }

        val playPendingIntent = PendingIntent.getService(
            this,
            PLAY_REQUEST_CODE,
            playActionIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)

        val forwardActionIntent = Intent(this, CounterService::class.java).apply {
            action = FORWARD_ACTION_CODE
        }

        val forwardPendingIntent = PendingIntent.getService(
            this,
            FORWARDS_REQUEST_CODE,
            forwardActionIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)

        return listOf(backPendingIntent, playPendingIntent, forwardPendingIntent)
    }

    private val actions: List<Any>
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            listOf(
                Notification.Action.Builder(
                    Icon.createWithResource(this, android.R.drawable.ic_media_previous),
                    "Back",
                    actionIntents[0])
                    .build(),
                Notification.Action.Builder(
                    Icon.createWithResource(
                        this,
                        if (_isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play),
                        if (_isPlaying) "Pause" else "Play",
                    actionIntents[1])
                    .build(),
                Notification.Action.Builder(
                    Icon.createWithResource(this, android.R.drawable.ic_media_next),
                    "Next",
                    actionIntents[2])
                    .build())
        } else {
            listOf(
                NotificationCompat.Action(android.R.drawable.ic_media_previous, "Back", actionIntents[0]),
                NotificationCompat.Action(android.R.drawable.ic_media_play, "Play", actionIntents[1]),
                NotificationCompat.Action(android.R.drawable.ic_media_next, "Next", actionIntents[2])
            )
        }
    }

    companion object {
        const val CHANNEL_ID = "1000"
        const val PLAY_REQUEST_CODE = 1001
        const val BACK_REQUEST_CODE = 1002
        const val FORWARDS_REQUEST_CODE = 1003
        const val START_SERVICE_ACTION_CODE = "START SERVICE"
        const val PLAY_ACTION_CODE = "PLAY"
        const val BACK_ACTION_CODE = "BACK"
        const val FORWARD_ACTION_CODE = "FORWARD"
    }
}