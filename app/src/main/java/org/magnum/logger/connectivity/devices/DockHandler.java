package org.magnum.logger.connectivity.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DockHandler extends BroadcastReceiver
{

	private static final String TAG = DockHandler.class.getCanonicalName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		long time = System.currentTimeMillis();
		try
		{

			int dock = intent.getIntExtra(Intent.EXTRA_DOCK_STATE, -1);
			new DeviceTable(context).insert(time, "Dock", dock);
			Log.d(TAG, "Dock event at " + time);
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
