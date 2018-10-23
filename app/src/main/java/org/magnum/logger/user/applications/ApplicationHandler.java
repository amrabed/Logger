package org.magnum.logger.user.applications;

import org.magnum.logger.Encryptor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ApplicationHandler extends BroadcastReceiver
{

	final static String TAG = "APPLICATION";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		long time = System.currentTimeMillis();
		try
		{
			String action = intent.getAction().substring(intent.getAction().indexOf("PACKAGE") + 8).toLowerCase();
			String appId = Encryptor.hash(intent.getData().getSchemeSpecificPart(), context);
			new ApplicationTable(context).insert(time, appId, action);
//			if (action.equals(Intent.ACTION_PACKAGE_ADDED))
//			{
//				new ApplicationTable(context).insert(time, appId, 0);
//			}
//			else if (action.equals(Intent.ACTION_PACKAGE_CHANGED))
//			{
//				new ApplicationTable(context).insert(time, appId, 1);
//			}
//			else if (action.equals(Intent.ACTION_PACKAGE_DATA_CLEARED))
//			{
//				new ApplicationTable(context).insert(time, appId, 2);
//			}
//			else if (action.equals(Intent.ACTION_PACKAGE_FULLY_REMOVED))
//			{
//				new ApplicationTable(context).insert(time, appId, 3);
//			}
//			else if (action.equals(Intent.ACTION_PACKAGE_REMOVED))
//			{
//				new ApplicationTable(context).insert(time, appId, 4);
//			}
//			else if (action.equals(Intent.ACTION_PACKAGE_REPLACED))
//			{
//				new ApplicationTable(context).insert(time, appId, 5);
//			}
//			else if (action.equals(Intent.ACTION_PACKAGE_RESTARTED))
//			{
//				new ApplicationTable(context).insert(time, appId, 6);
//				RunningApplicationHandler.packageList.remove(appId);
//			}
//			else if (action.equals(Intent.ACTION_PACKAGE_FIRST_LAUNCH))
//			{
//				new ApplicationTable(context).insert(time, appId, 7);
//			}
			Log.d(TAG, "Package " + appId + " " + action);
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
