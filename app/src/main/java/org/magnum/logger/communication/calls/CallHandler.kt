package org.magnum.logger.communication.calls

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class CallHandler : BroadcastReceiver() {
    private var startTime: Long = 0
    private var callTime: Long = 0

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val time = System.currentTimeMillis()
            if (Intent.ACTION_NEW_OUTGOING_CALL == intent.action) {
                callTime = time
                val phoneNumber = Encryptor.encryptPhoneNumber(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER), context)
                Repository(context).logCall(CallRecord(time, phoneNumber, false, null))
                Log.d(TAG, "Calling $phoneNumber at $time")
            } else {
                when (intent.getStringExtra(TelephonyManager.EXTRA_STATE)) {
                    TelephonyManager.EXTRA_STATE_RINGING -> {
                        callTime = time
                        val phoneNumber = Encryptor.encryptPhoneNumber(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER), context)
                        Repository(context).logCall(CallRecord(time, phoneNumber, true, null))
                        Log.d(TAG, "Call received from $phoneNumber at $time")
                    }
                    TelephonyManager.EXTRA_STATE_IDLE -> {
                        Repository(context).updateCall(callTime, time - startTime)
                        Log.d(TAG, "Call ended at $time")
                    }
                    else -> {
                        startTime = time
                        Log.d(TAG, "Ongoing call at $time")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = CallHandler::class.java.canonicalName
    }
}