package org.magnum.logger.user.applications;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.magnum.logger.Encryptor;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class RunningApplicationHandler extends Service
{

	final static String TAG = "APPLICATION";
	static Collection<String> packageList = new ArrayList<String>();

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

//					List<RecentTaskInfo> tasks = activityManager.getRecentTasks(Integer.MAX_VALUE, 2);
//
//					for (RecentTaskInfo task : tasks)
//					{
//						String packageName = task.origActivity.getPackageName();
//						if (!packageList.contains(packageName))
//						{
//							new ApplicationTable(getApplicationContext()).insert(System.currentTimeMillis(), packageName, "LAUNCHED");
//							Log.d(TAG, "Package " + packageName + " launched");
//							packageList.add(packageName);
//						}
//					}
//
					final List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
					for (int i = 0; i < runningTasks.size(); i++)
					{
						String packageName = runningTasks.get(i).baseActivity.getPackageName();
						if (!packageList.contains(packageName))
						{
							new ApplicationTable(getApplicationContext()).insert(System.currentTimeMillis(), Encryptor.hash(packageName, getApplicationContext()), "launched");
							Log.d(TAG, "Package " + packageName + " launched");
							packageList.add(runningTasks.get(i).baseActivity.getPackageName());
						}
					}
					//
					// List<RunningAppProcessInfo> procInfos =
					// activityManager.getRunningAppProcesses();
					// for (int i = 0; i < procInfos.size(); i++)
					// {
					//
					// ArrayList<String> runningPkgs = new
					// ArrayList<String>(Arrays.asList(procInfos.get(i).pkgList));
					//
					// Collection<String> diff = subtractSets(runningPkgs,
					// packageList);
					//
					// if (diff != null)
					// {
					// packageList.removeAll(diff);
					// }
					// }
					//
					// for(String appId: packageList)
					// {
					// new
					// ApplicationTable(getApplicationContext()).insert(System.currentTimeMillis(),
					// appId, "LAUNCHED");
					// Log.d(TAG, "Package " + appId + " launched");
					// }

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

	public static Collection<String> subtractSets(Collection<String> a, Collection<String> b)
	{
		Collection<String> result = new ArrayList<String>(b);
		result.removeAll(a);
		return result;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
}
