package com.brunoponte.everythingdroid.ui.servicesPlayground

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast

class CounterBackgroundService : Service() {
    private var counter: Int = 0

    override fun onCreate() {
        Toast.makeText(this, "service created", Toast.LENGTH_SHORT).show()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service started", Toast.LENGTH_SHORT).show()

        startCounterLoop()

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service destroyed", Toast.LENGTH_SHORT).show()
    }

    private fun startCounterLoop() {
        val handler = Handler(Looper.getMainLooper())
        val run = object : Runnable {
            override fun run() {
                counter += 1
                sendBroadcast()
                handler.postDelayed(this, 1000)
            }
        }

        handler.post(run)
    }

    private fun sendBroadcast() {
        Intent().also { intent ->
            intent.action = BROADCAST_TRANSMITTER_ID
            intent.putExtra(BROADCAST_COUNTER_ID, counter)
            sendBroadcast(intent)
        }
    }

    companion object {

        val BROADCAST_TRANSMITTER_ID = this::class.simpleName
        const val BROADCAST_COUNTER_ID = "counter"

    }
}