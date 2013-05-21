package org.magnum.logger.device.power;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

public class PowerHandler extends BroadcastReceiver
{

	final static String TAG = "POWER";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

			long time = System.currentTimeMillis();
			int state = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			float level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) / (float) intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
			int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

			new PowerTable(context).insert(time, state, level, plugged);

			Log.d(TAG, "Battery level: " + level);
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
