package org.magnum.logger.device.power;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.util.Log;

public class PowerHandler extends BroadcastReceiver
{

	private static final String TAG = PowerHandler.class.getCanonicalName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			long time = System.currentTimeMillis();
			if (intent != null)
			{
				int state = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
				float level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) / (float) intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
				int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

				new PowerTable(context).insert(time, state, level, plugged);

				Log.d(TAG, "Battery level: " + level);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
