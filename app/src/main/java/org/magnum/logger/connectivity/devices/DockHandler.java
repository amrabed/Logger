package org.magnum.logger.connectivity.devices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DockHandler extends BroadcastReceiver
{

	final static String TAG = "DOCK";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		long time = System.currentTimeMillis();
		try
		{
			//String action = intent.getAction();

			//if (action.equals(Intent.ACTION_DOCK_EVENT))
			{
				int dock = intent.getIntExtra(Intent.EXTRA_DOCK_STATE, -1);
				new DeviceTable(context).insert(time, "Dock" , dock);
				Log.d(TAG, "Dock event at " + time);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
