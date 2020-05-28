package org.magnum.logger.user.applications

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class RunningApplicationHandler : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            // TODO not working properly
            for (task in activityManager.getRunningTasks(Int.MAX_VALUE)) {
                val packageName = task.baseActivity?.packageName
                Repository(applicationContext).logApp(AppRecord(System.currentTimeMillis(), Encryptor.hash(packageName), "launched"))
                Log.d(TAG, "Package $packageName launched")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private val TAG = RunningApplicationHandler::class.java.canonicalName
    }
}