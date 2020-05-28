package org.magnum.logger.communication.email

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class EmailHandler(private val context: Context, handler: Handler?) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        onChange(selfChange, Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, null))
    }

    override fun onChange(selfChange: Boolean, uri: Uri) {
        super.onChange(selfChange, uri)
        try {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.moveToNext()
            val time = cursor?.getLong(cursor.getColumnIndex("date"))
            val id = Encryptor.encrypt(cursor?.getString(cursor.getColumnIndex("address")), context)
            cursor?.close()
            if (time != null) Repository(context).logEmail(EmailRecord(time, id))
            Log.d(TAG, "Email  from/to $id at $time")
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = EmailHandler::class.java.canonicalName
    }

}