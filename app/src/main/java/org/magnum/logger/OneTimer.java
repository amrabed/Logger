package org.magnum.logger;

import org.magnum.logger.user.applications.RunningApplicationHandler;
import org.magnum.logger.user.location.LocationHandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

public class OneTimer extends Service
{
	final static String TAG = "ONETIME";

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		try
		{
			monitorLocation();
			monitorApps();
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
		return START_NOT_STICKY;
	}



	private void monitorLocation()
	{
		LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1610, new LocationHandler(getApplicationContext()));
	}

	private void monitorApps()
	{
		PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, new Intent(this, RunningApplicationHandler.class), 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES / 15, pendingIntent);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
