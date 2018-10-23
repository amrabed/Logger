package org.magnum.logger.communication.calls;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.magnum.logger.Encryptor;

public class CallHandler extends BroadcastReceiver
{

	private static final String TAG = CallHandler.class.getCanonicalName();

	private static final int OUTGOING = 0;
	private static final int INCOMING = 1;

	private long startTime;
	private long callTime;

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			long time = System.currentTimeMillis();
			if (Intent.ACTION_NEW_OUTGOING_CALL.equals(intent.getAction()))
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
					new CallTable(context).setDuration(callTime, time - startTime);
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
}
