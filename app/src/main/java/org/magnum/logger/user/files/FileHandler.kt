package org.magnum.logger.user.files

import android.content.Context
import android.os.FileObserver
import android.util.Log
import org.magnum.logger.Encryptor
import org.magnum.logger.Repository

class FileHandler(private val context: Context, private val path: String) : FileObserver(path) {
    override fun onEvent(event: Int, file: String?) {
        try {
            val time = System.currentTimeMillis()
            val fullPath = "$path/$file"
            Repository(context).logFile(FileRecord(time, Encryptor.encrypt(fullPath, context), event))
            Log.d(TAG, "File $fullPath accessed at $time")
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = FileHandler::class.java.canonicalName
    }

}