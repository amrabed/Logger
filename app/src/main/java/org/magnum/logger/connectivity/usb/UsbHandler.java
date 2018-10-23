package org.magnum.logger.connectivity.usb;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.util.Log;

import org.magnum.logger.Encryptor;

import java.util.HashMap;
import java.util.Set;

public class UsbHandler extends BroadcastReceiver
{
	private static final String TAG = UsbHandler.class.getCanonicalName();

	@SuppressLint("DefaultLocale")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public void onReceive(Context context, Intent intent)
	{
		long time = System.currentTimeMillis();
		try
		{
			String action = intent.getAction();
			if (action != null)
			{
				if (action.contains("UMS"))
				{
					String description = action.substring(action.indexOf("UMS") + 4).toLowerCase();

					Log.d(TAG, "UMS device connected/disconnected");
					// TODO: not working correctly
					UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
					HashMap<String, UsbDevice> devices = manager.getDeviceList();
					Set<String> keySet = devices.keySet();
					for (String key : keySet)
					{
						UsbDevice device = devices.get(key);
						String name = Encryptor.encrypt(device.getDeviceName(), context);
						new UsbTable(context).insert(time, name, device.getDeviceClass(), description);
						Log.d(TAG, "UMS device connected/disconnected");
					}
				}
				else
				{
					String description = action.substring(action.indexOf("USB") + 4).toLowerCase();

					if (description.contains("ACCESSORY"))
					{
						UsbAccessory accessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
						String id = Encryptor.encrypt(accessory.getSerial(), context);
						new UsbTable(context).insert(time, id, accessory.getDescription(), description.substring(description.indexOf("ACCESSORY") + 10).toLowerCase());
					}
					else
					{
						UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
						String name = Encryptor.encrypt(device.getDeviceName(), context);
						new UsbTable(context).insert(time, name, device.getDeviceClass(), description.substring(description.indexOf("DEVICE") + 7).toLowerCase());
					}
					Log.d(TAG, "USB device connected/disconnected");
				}
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
