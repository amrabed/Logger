package org.magnum.logger.user.browsing

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.util.Log
import androidx.preference.PreferenceManager
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class BrowserHandler(private val context: Context, handler: Handler?) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean, uri: Uri) {
        super.onChange(selfChange)
        try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            while (cursor != null && cursor.moveToNext()) {
                val time = cursor.getLong(0)
                val url = cursor.getString(1)
                val i = url.indexOf('/')
                val j = url.indexOf('/', i + 2)
                val domain = if (j != -1) url.substring(i + 2, j) else url.substring(i + 2)
                if (time > PreferenceManager.getDefaultSharedPreferences(context).getLong(TAG, 0)) {
                    Repository(context).logBrowser(BrowserRecord(time, Encryptor.encrypt(domain, context)))
                    PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(TAG, time).apply()
                    Log.d(TAG, "URL $domain visited at $time")
                }
            }
            cursor?.close()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    override fun deliverSelfNotifications(): Boolean {
        return false
    }

    companion object {
        private val TAG = BrowserHandler::class.java.canonicalName
    }
}