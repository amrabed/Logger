package org.magnum.logger.user.applications;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.magnum.logger.Encryptor;

import java.util.ArrayList;
import java.util.List;

public class RunningApplicationHandler extends Service
{

	private static final String TAG = RunningApplicationHandler.class.getCanonicalName();
	private static ArrayList<String> packageList = new ArrayList<String>();

	public static List<String> subtractSets(List<String> a, List<String> b)
	{
		ArrayList<String> result = new ArrayList<String>(b);
		result.removeAll(a);
		return result;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{

		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					// TODO not working properly

					if (activityManager != null)
					{
						final List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

						for (int i = 0; i < runningTasks.size(); i++)
						{
							String packageName = runningTasks.get(i).baseActivity.getPackageName();
							if (!packageList.contains(packageName))
							{
								new ApplicationTable(getApplicationContext()).insert(System.currentTimeMillis(), Encryptor.hash(packageName), "launched");
								Log.d(TAG, "Package " + packageName + " launched");
								packageList.add(runningTasks.get(i).baseActivity.getPackageName());
							}
						}
					}
				}
				catch (Exception e)
				{
					Log.e(TAG,e.toString());
				}
			}
		});
		t.start();

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
}
