package org.magnum.logger.user.applications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.magnum.logger.Encryptor;

public class ApplicationHandler extends BroadcastReceiver
{

	private static final String TAG = ApplicationHandler.class.getCanonicalName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		long time = System.currentTimeMillis();
		try
		{
			String action = intent.getAction();
			if (action != null)
			{
				String change = action.substring(action.indexOf("PACKAGE") + 8).toLowerCase();
				Uri data = intent.getData();
				if (data != null)
				{
					String appId = Encryptor.hash(data.getSchemeSpecificPart(), context);
					new ApplicationTable(context).insert(time, appId, action);
					Log.d(TAG, "Package " + appId + " " + change);
				}
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
