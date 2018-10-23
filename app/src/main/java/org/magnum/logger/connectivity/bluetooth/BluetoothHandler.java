package org.magnum.logger.connectivity.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.magnum.logger.Encryptor;

public class BluetoothHandler extends BroadcastReceiver
{

	private static final String TAG = BluetoothHandler.class.getCanonicalName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		long time = System.currentTimeMillis();
		try
		{
			String action = intent.getAction();
			String change = action.substring(action.lastIndexOf('.') + 1);
			BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			String name = Encryptor.encrypt(device.getName(),context);
			String address = Encryptor.encrypt(device.getAddress(),context);
			new BluetoothTable(context).insert(time, name, address, device.getBluetoothClass().getMajorDeviceClass(), change);
			Log.d(TAG, "Device " + device.getAddress() + " connected/disconnected at " + time);
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
