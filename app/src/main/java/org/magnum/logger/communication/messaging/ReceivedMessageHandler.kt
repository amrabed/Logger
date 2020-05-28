package org.magnum.logger.communication.messaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class ReceivedMessageHandler : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        try {
            var time = System.currentTimeMillis()
            var id: String? = ""
            val pdus = intent.extras!!["pdus"] as Array<*>?
            val msgs = arrayOfNulls<SmsMessage>(pdus!!.size)
            for (i in msgs.indices) {
                msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                time = msgs[i]!!.timestampMillis
                id = Encryptor.encryptPhoneNumber(msgs[i]!!.originatingAddress, context)

                Repository(context).logMessage(MessageRecord(time, id, true))
            }
            Log.d(TAG, "SMS received from $id at $time")
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = ReceivedMessageHandler::class.java.canonicalName
    }
}