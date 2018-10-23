package org.magnum.logger.communication.messaging;

import org.magnum.logger.Encryptor;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class MessageListReader extends Service
{
	final static String TAG = "MESSAGE_";

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
					Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
					while (cursor.moveToNext())
					{
						int type = cursor.getInt(cursor.getColumnIndex("type")) % 2;
						String number = Encryptor.encryptPhoneNumber(cursor.getString(cursor.getColumnIndex("address")), getApplicationContext());
						Long time = cursor.getLong(cursor.getColumnIndex("date"));

						new MessageTable(getApplicationContext()).insert(time, number, type);
						Log.d(TAG, "Adding message to/from " + number + " at time " + time);
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
