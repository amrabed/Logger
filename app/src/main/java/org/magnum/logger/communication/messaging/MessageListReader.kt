package org.magnum.logger.communication.messaging

import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class MessageListReader : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            val cursor = contentResolver.query(Uri.parse("content://sms"), null, null, null, null)
            while (cursor != null && cursor.moveToNext()) {
                val type = cursor.getInt(cursor.getColumnIndex("type")) % 2
                val number = Encryptor.encryptPhoneNumber(cursor.getString(cursor.getColumnIndex("address")), applicationContext)
                val time = cursor.getLong(cursor.getColumnIndex("date"))
                Repository(applicationContext).logMessage(MessageRecord(time, number, type == 1))
                Log.d(TAG, "Adding message to/from $number at time $time")
            }
            cursor?.close()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private val TAG = MessageListReader::class.java.canonicalName
    }
}