package org.magnum.logger.connectivity.wifi

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.IBinder
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class RegisteredNetworksReader : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            val time = System.currentTimeMillis()
            val manager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiList = manager.configuredNetworks
            for (wifi in wifiList) {
                Repository(applicationContext).logWifi(WifiRecord(time, Encryptor.encrypt(wifi.BSSID, applicationContext)))
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return START_NOT_STICKY
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    companion object {
        private val TAG = RegisteredNetworksReader::class.java.canonicalName
    }
}