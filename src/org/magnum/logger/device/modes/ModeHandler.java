package org.magnum.logger.device.modes;

import android.app.UiModeManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

public class ModeHandler extends BroadcastReceiver
{

	final static String TAG = "MODE";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		long time = System.currentTimeMillis();
		try
		{
			if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED))
			{
				if (intent.getBooleanExtra("state", false))
				{
					new ModeTable(context).insert(time, 4);
					Log.d(TAG, "Airplane Mode activated at " + time);
				}
				else
				{
					new ModeTable(context).insert(time, 3);
					Log.d(TAG, "Airplane Mode deactivated at " + time);
				}
			}
			else if (intent.getAction().equals(Intent.ACTION_DOCK_EVENT))
			{
				UiModeManager uiMode = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
				new ModeTable(context).insert(time, uiMode.getCurrentModeType() + 4);
				Log.d(TAG, "UI mode changed at " + time);
			}
			else
			{
				new ModeTable(context).insert(time, intent.getIntExtra(AudioManager.EXTRA_RINGER_MODE, -1));
				Log.d(TAG, "Ringer mode changed at " + time);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
