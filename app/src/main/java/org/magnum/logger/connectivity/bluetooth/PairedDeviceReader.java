package org.magnum.logger.connectivity.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.magnum.logger.Encryptor;

import java.util.Set;

public class PairedDeviceReader extends Service
{
	private static final String TAG = PairedDeviceReader.class.getCanonicalName();

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
					BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

					for (BluetoothDevice device : devices)
					{
						String name = Encryptor.encrypt(device.getName(),getApplicationContext());
						String address = Encryptor.encrypt(device.getAddress(),getApplicationContext());
						new BluetoothTable(getApplicationContext()).insert(time, name, address, device.getBluetoothClass().getMajorDeviceClass(), "added");
						Log.d(TAG, "Device " + device.getAddress() + " added at " + time);
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
	public IBinder onBind(Intent intent)
	{
		return null;
	}

}
