package org.magnum.logger.connectivity.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.os.Parcelable
import android.util.Log
import androidx.preference.PreferenceManager
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository
import org.magnum.logger.SyncService

class WifiHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            val time = System.currentTimeMillis()
            if (intent.getParcelableExtra<Parcelable>(WifiManager.EXTRA_NEW_STATE) == SupplicantState.COMPLETED) {
                val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                val info = wifi.connectionInfo
                val macAddress = Encryptor.encrypt(info.bssid, context)
                if (PreferenceManager.getDefaultSharedPreferences(context).getString(TAG, "") != macAddress) {
                    Repository(context).logWifi(WifiRecord(time, macAddress))
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString(TAG, macAddress).apply()
                }
                context.startService(Intent(context, SyncService::class.java))
                Log.d(TAG, "Connected to $macAddress at $time")
            } else if (intent.getParcelableExtra<Parcelable>(WifiManager.EXTRA_NEW_STATE) == SupplicantState.DISCONNECTED) {
                if (PreferenceManager.getDefaultSharedPreferences(context).getString(TAG, "") != DISCONNECTED) {
                    Repository(context).logWifi(WifiRecord(time, DISCONNECTED))
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString(TAG, DISCONNECTED).apply()
                }
                Log.d(TAG, "Disconnected at $time")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = WifiHandler::class.java.canonicalName
        private val DISCONNECTED = SupplicantState.DISCONNECTED.toString()
    }
}