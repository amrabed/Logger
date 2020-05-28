package org.magnum.logger.connectivity.devices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.preference.PreferenceManager
import org.magnum.logger.Repository

class HeadsetHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val time = System.currentTimeMillis()
            val isPlugged = intent.getIntExtra("state", 0)
            if (PreferenceManager.getDefaultSharedPreferences(context).getInt(TAG, -1) != isPlugged) {
                Repository(context).logDevice(DeviceRecord(time, intent.getStringExtra("name")!!, intent.getIntExtra("state", 0)))
                PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(TAG, isPlugged).apply()
                Log.d(TAG, "Headset plugged/unplugged at $time")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = HeadsetHandler::class.java.canonicalName
    }
}