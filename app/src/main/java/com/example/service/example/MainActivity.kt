package com.example.service.example

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var numberService: RandomNumberService
    private var serviceBounded: Boolean = false

    /** Callbacks for service binding */
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // Service connection to LocalService was successful,
            // cast the IBinder and save LocalService instance.
            val binder = service as RandomNumberService.LocalBinder
            numberService = binder.getService()
            serviceBounded = true
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            serviceBounded = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        Intent(this, RandomNumberService::class.java).also { intent ->
            // Bind service through connection asynchronously
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onResume() {
        super.onResume()
        findViewById<TextView>(R.id.btnRandomNumber).setOnClickListener {
            if (!serviceBounded) return@setOnClickListener
            // Call service for a random number
            updateUi(numberService.randomNumber.toString())
        }
    }

    override fun onStop() {
        super.onStop()
        unbindService(connection)
        serviceBounded = false
    }

    private fun updateUi(value: String) {
        findViewById<TextView>(R.id.tvRandomNumber).text = value
    }
}