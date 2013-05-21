package org.magnum.logger.communication.calls;

import org.magnum.logger.Encryptor;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.CallLog;
import android.util.Log;

public class CallLogReader extends Service
{
	final static String TAG = "CALL_";

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
					Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
					while (cursor.moveToNext())
					{
						long time = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
						String number = Encryptor.encryptPhoneNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)), getApplicationContext());
						int isReceived = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)) % 2;
						long duration = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DURATION)) * 1000;

						new CallTable(getApplicationContext()).insert(time, number, isReceived, duration);
						Log.d(TAG, "Adding call with " + number + " at time " + time);
					}
					cursor.close();
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
