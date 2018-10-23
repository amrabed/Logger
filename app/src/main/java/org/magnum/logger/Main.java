package org.magnum.logger;

import org.magnum.logger.communication.calls.CallLogReader;
import org.magnum.logger.communication.messaging.MessageListReader;
import org.magnum.logger.connectivity.bluetooth.PairedDeviceReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class Main extends Activity
{
	final static String TAG = "firstTime";
	static boolean isAlreadyRunning = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{
			if (isAlreadyRunning)
			{
				Toast.makeText(this, "Application already running in background", Toast.LENGTH_LONG).show();
				startService(new Intent(this, SyncService.class));
				finish();
			}
			else
			{
				isAlreadyRunning = true;

				if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(TAG, true))
				{
					PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(TAG, false).commit();
					startService(new Intent(this, CallLogReader.class));
					startService(new Intent(this, MessageListReader.class));
					startService(new Intent(this, PairedDeviceReader.class));
					startActivity(new Intent(this, MapActivity.class));
				}
				else
				{
					Toast.makeText(this, "Application is running in background", Toast.LENGTH_LONG).show();
				}
				startService(new Intent(this, OneTimer.class));
				startService(new Intent(this, Controller.class));
				finish();
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}
}