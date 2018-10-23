package org.magnum.logger.connectivity.wifi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import org.magnum.logger.Encryptor;

import java.util.List;

public class RegisteredNetworksReader extends Service
{
	private static final String TAG = RegisteredNetworksReader.class.getCanonicalName();

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
					long time = System.currentTimeMillis();
					final WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
					if (manager != null)
					{
						final List<WifiConfiguration> wifiList = manager.getConfiguredNetworks();
						for (WifiConfiguration wifi : wifiList)
						{
							new WifiTable(getApplicationContext()).insert(time, Encryptor.encrypt(wifi.BSSID, getApplicationContext()));
						}
					}
				}
				catch (Exception e)
				{
					Log.e(TAG, e.toString());
				}
			}
		});
		t.start();
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

}
