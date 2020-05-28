package org.magnum.logger

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import org.magnum.logger.user.applications.RunningApplicationHandler
import org.magnum.logger.user.location.LocationHandler

class OneTimer : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            monitorLocation()
            monitorApps()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return START_NOT_STICKY
    }

    private fun monitorLocation() {
        val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1610f, LocationHandler(applicationContext))
        }
    }

    private fun monitorApps() {
        val pendingIntent = PendingIntent.getService(applicationContext, 0, Intent(this, RunningApplicationHandler::class.java), 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15, pendingIntent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private val TAG = OneTimer::class.java.canonicalName
    }
}