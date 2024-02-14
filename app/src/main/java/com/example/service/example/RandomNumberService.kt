package com.example.service.example

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.Random

class RandomNumberService : Service() {

    // Binder given to clients.
    private val binder = LocalBinder()

    // Random number generator.
    private val mGenerator = Random()

    /**
     * request next random number from client
     */
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    /**
     * Class used for client Binder. Because this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        // Return this instance of LocalService so clients can call public methods.
        fun getService(): RandomNumberService = this@RandomNumberService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

}