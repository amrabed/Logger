package org.magnum.logger.connectivity.mobile

import android.content.Context
import android.net.ConnectivityManager
import android.telephony.CellLocation
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.telephony.cdma.CdmaCellLocation
import android.telephony.gsm.GsmCellLocation
import android.util.Log
import androidx.preference.PreferenceManager
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository
import org.magnum.logger.device.modes.ModeHandler

class MobileHandler(private val context: Context) : PhoneStateListener() {
    override fun onCellLocationChanged(location: CellLocation) {
        try {
            val time = System.currentTimeMillis()
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val type = info?.subtype
            val id = if (type == TelephonyManager.NETWORK_TYPE_CDMA) {
                (location as CdmaCellLocation).baseStationId
            } else {
                (location as GsmCellLocation).cid
            }
            if (id != PreferenceManager.getDefaultSharedPreferences(context).getInt(TAG, -2)) {
                Repository(context).logMobile(MobileRecord(time, Encryptor.encrypt(id.toString(), context), info?.subtypeName))
                PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(TAG, id).apply()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = ModeHandler::class.java.canonicalName
    }
}