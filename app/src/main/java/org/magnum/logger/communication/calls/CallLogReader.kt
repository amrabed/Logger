package org.magnum.logger.communication.calls

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.provider.CallLog
import android.util.Log
import androidx.core.app.ActivityCompat
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class CallLogReader : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            try {
                val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, null, null, null, null)
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val time = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE))
                        val number = Encryptor.encryptPhoneNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)), applicationContext)
                        val isReceived = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) % 2
                        val duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)) * 1000
                        Repository(applicationContext).logCall(CallRecord(time, number, isReceived == 1, duration))
                        Log.d(TAG, "Adding call with $number at time $time")
                    }
                    cursor.close()
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private val TAG = CallLogReader::class.java.canonicalName
    }
}