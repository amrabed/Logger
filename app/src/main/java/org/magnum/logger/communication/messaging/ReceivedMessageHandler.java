package org.magnum.logger.communication.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import org.magnum.logger.Encryptor;

public class ReceivedMessageHandler extends BroadcastReceiver
{

	private static final String TAG = ReceivedMessageHandler.class.getCanonicalName();

	@Override
	public void onReceive(Context context, Intent intent)
	{
		try
		{
			long time = System.currentTimeMillis();
			String id = "";
			Bundle bundle = intent.getExtras();
			Object[] pdus = (Object[]) bundle.get("pdus");
			SmsMessage[] msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++)
			{
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				time = msgs[i].getTimestampMillis();
				id = Encryptor.encryptPhoneNumber(msgs[i].getOriginatingAddress(), context);
				new MessageTable(context).insert(time, id, 1);
			}
			Log.d(TAG, "SMS received from " + id + " at " + time);

		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}
}
