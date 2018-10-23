package org.magnum.logger.communication.messaging;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import org.magnum.logger.Encryptor;

public class SentMessageHandler extends ContentObserver
{
	private static final String TAG = SentMessageHandler.class.getCanonicalName();
	private static final String ADDRESS = "address";
	private static final String TIME = "time";

	private Context context;

	public SentMessageHandler(Context context, Handler handler)
	{
		super(handler);
		this.context = context;
	}

	@Override
	public void onChange(boolean selfChange)
	{
		super.onChange(selfChange);
		long time;
		try
		{
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/sent/"), null, null, null, null);
			if (cursor != null)
			{
				cursor.moveToNext();

				time = cursor.getLong(cursor.getColumnIndex("date"));
				String id = Encryptor.encryptPhoneNumber(cursor.getString(cursor.getColumnIndex(ADDRESS)), context);
				if ((PreferenceManager.getDefaultSharedPreferences(context).getLong(TAG + TIME, 0) != time) || (!PreferenceManager.getDefaultSharedPreferences(context).getString(TAG + ADDRESS, "").equals(id)))
				{
					// Temporary Work-around for repeated call
					new MessageTable(context).insert(time, id, 0);
					Log.d(TAG, "Message sent to " + id + " at " + time);
					PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(TAG + TIME, time).commit();
					PreferenceManager.getDefaultSharedPreferences(context).edit().putString(TAG + ADDRESS, id).commit();
				}
				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}

	}
}
