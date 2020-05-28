package org.magnum.logger.user.applications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository
import java.util.*

class ApplicationHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val action = intent.action
            if (action != null) {
                val time = System.currentTimeMillis()
                val change = action.substring(action.indexOf("PACKAGE") + 8).toLowerCase(Locale.getDefault())
                val data = intent.data
                if (data != null) {
                    val appId = Encryptor.hash(data.schemeSpecificPart)
                    Repository(context).logApp(AppRecord(time, appId, action))
                    Log.d(TAG, "Package $appId $change")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = ApplicationHandler::class.java.canonicalName
    }
}