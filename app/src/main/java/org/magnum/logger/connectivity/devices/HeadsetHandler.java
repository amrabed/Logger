package org.magnum.logger.connectivity.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class HeadsetHandler extends BroadcastReceiver
{

	private static final String TAG = HeadsetHandler.class.getCanonicalName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		long time = System.currentTimeMillis();
		try
		{
			int isPlugged = intent.getIntExtra("state", 0);
			if (PreferenceManager.getDefaultSharedPreferences(context).getInt(TAG, -1) != isPlugged)
			{
				new DeviceTable(context).insert(time, intent.getStringExtra("name"), intent.getIntExtra("state", 0));
				PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(TAG, isPlugged).commit();
				Log.d(TAG, "Headset plugged/unplugged at " + time);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
