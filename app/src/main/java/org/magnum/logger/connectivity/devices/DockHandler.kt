package org.magnum.logger.connectivity.devices

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.magnum.logger.Repository

class DockHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_DOCK_EVENT == intent.action) {
            try {
                val time = System.currentTimeMillis()
                val dock = intent.getIntExtra(Intent.EXTRA_DOCK_STATE, -1)
                Repository(context).logDevice(DeviceRecord(time, "Dock", dock))
                Log.d(TAG, "Dock event at $time")
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    companion object {
        private val TAG = DockHandler::class.java.canonicalName
    }
}