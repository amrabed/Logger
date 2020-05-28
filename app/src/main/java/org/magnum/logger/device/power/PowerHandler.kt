package org.magnum.logger.device.power

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import org.magnum.logger.Repository

class PowerHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
            try {
                val time = System.currentTimeMillis()
                val state = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) / intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1).toFloat()
                val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                Repository(context).logPower(PowerRecord(time, state, level, plugged == 1))
                Log.d(TAG, "Battery level: $level")
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    companion object {
        private val TAG = PowerHandler::class.java.canonicalName
    }
}