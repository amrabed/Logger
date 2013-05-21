package org.magnum.logger.communication.calls;

import org.magnum.logger.Encryptor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallHandler extends BroadcastReceiver
{

	final static String TAG = "CALL";
	static long startTime, endTime;
	static long callTime;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			long time = System.currentTimeMillis();
			if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
			{
				callTime = time;
				String phoneNumber = Encryptor.encryptPhoneNumber(intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER), context);
				new CallTable(context).insert(time, phoneNumber, OUTGOING);
				Log.d(TAG, "Calling " + phoneNumber + " at " + time);
			}
			else
			{
				String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
				if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))
				{
					callTime = time;
					String phoneNumber = Encryptor.encryptPhoneNumber(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER), context);
					new CallTable(context).insert(time, phoneNumber, INCOMING);
					Log.d(TAG, "Call received from " + phoneNumber + " at " + time);
				}
				else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
				{
					endTime = time;
					new CallTable(context).setDuration(callTime, endTime - startTime);
					Log.d(TAG, "Call ended at " + time);
				}
				else
				{
					startTime = time;
					Log.d(TAG, "Ongoing call at " + time);
				}
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

	final static int OUTGOING = 0;
	final static int INCOMING = 1;
}
