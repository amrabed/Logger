package org.magnum.logger

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Encryptor {
    private const val TAG = "ENCRYPT"
    private const val ENCODING = "US-ASCII"
    private const val ALGORITHM = "HmacSHA256"
    fun encryptPhoneNumber(number: String?, context: Context?): String {
        // Keep digits only
        return encrypt(number!!.replace("\\D+".toRegex(), ""), context)
    }

    fun encrypt(data: String?, context: Context?): String {
        try {
            if (data != null) {
                val mac = Mac.getInstance(ALGORITHM)
                mac.init(SecretKeySpec(data.toByteArray(charset(ENCODING)), ALGORITHM))
                return String(mac.doFinal(getSalt(context).toString().toByteArray(charset(ENCODING))), charset(ENCODING))
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return ""
    }

    fun hash(data: String?): String {
        try {
            if (data != null) {
                val mac = Mac.getInstance(ALGORITHM)
                mac.init(SecretKeySpec(data.toByteArray(charset(ENCODING)), ALGORITHM))
                return String(mac.doFinal(), charset(ENCODING))
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return ""
    }

    private fun getSalt(context: Context?): Int {
        val salt: Int
        if (PreferenceManager.getDefaultSharedPreferences(context).contains(TAG)) {
            salt = PreferenceManager.getDefaultSharedPreferences(context).getInt(TAG, 0)
        } else {
            salt = Random(System.currentTimeMillis()).nextInt()
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(TAG, salt).apply()
        }
        return salt
    }
}