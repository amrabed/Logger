package org.magnum.logger.connectivity.wifi;

import org.magnum.logger.Encryptor;
import org.magnum.logger.SyncService;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class WifiHandler extends BroadcastReceiver
{

	final static String TAG = "WIFI";
	final static String DISCONNECTED = SupplicantState.DISCONNECTED.toString();

	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent)
	{
		long time = System.currentTimeMillis();
		try
		{
			if (intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE).equals(SupplicantState.COMPLETED))
			{
				WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo info = wifi.getConnectionInfo();
				String macAddress = Encryptor.encrypt(info.getBSSID(), context);
				if(!PreferenceManager.getDefaultSharedPreferences(context).getString(TAG, "").equals(macAddress))
				{
					new WifiTable(context).insert(time, macAddress);
					PreferenceManager.getDefaultSharedPreferences(context).edit().putString(TAG, macAddress).commit();
				}
				
				context.startService(new Intent(context, SyncService.class));
				Log.d(TAG, "Connected to " + macAddress + " at " + time);
			}
			else if (intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE).equals(SupplicantState.DISCONNECTED))
			{
				if(!PreferenceManager.getDefaultSharedPreferences(context).getString(TAG, "").equals(DISCONNECTED))
				{
					new WifiTable(context).insert(time, SupplicantState.DISCONNECTED);
					PreferenceManager.getDefaultSharedPreferences(context).edit().putString(TAG, DISCONNECTED).commit();
				}
				Log.d(TAG, "Disconnected at " + time);
			}
//			else if (intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE).equals(SupplicantState.DORMANT))
//			{
//				new WifiTable(context).insert(time, SupplicantState.DORMANT);
//				Log.d(TAG, "Explicitly disconnected at " + time);
//			}
//			else if (intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE).equals(SupplicantState.INTERFACE_DISABLED))
//			{
//				new WifiTable(context).insert(time, SupplicantState.INTERFACE_DISABLED);
//				Log.d(TAG, "Disabled at " + time);
//			}				
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
