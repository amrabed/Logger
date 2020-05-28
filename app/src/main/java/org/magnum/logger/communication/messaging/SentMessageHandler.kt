package org.magnum.logger.communication.messaging

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.preference.PreferenceManager
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class SentMessageHandler(private val context: Context, handler: Handler?) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        try {
            val cursor = context.contentResolver.query(Uri.parse("content://sms/sent/"), null, null, null, null)
            if (cursor != null) {
                cursor.moveToNext()
                val time = cursor.getLong(cursor.getColumnIndex("date"))
                val id = Encryptor.encryptPhoneNumber(cursor.getString(cursor.getColumnIndex(ADDRESS)), context)
                if (PreferenceManager.getDefaultSharedPreferences(context).getLong(TAG + TIME, 0) != time || PreferenceManager.getDefaultSharedPreferences(context).getString(TAG + ADDRESS, "") != id) {
                    // Temporary Work-around for repeated call
                    Repository(context).logMessage(MessageRecord(time, id, false))
                    Log.d(TAG, "Message sent to $id at $time")
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(TAG + TIME, time).apply()
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putString(TAG + ADDRESS, id).apply()
                }
                cursor.close()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = SentMessageHandler::class.java.canonicalName
        private const val ADDRESS = "address"
        private const val TIME = "time"
    }

}