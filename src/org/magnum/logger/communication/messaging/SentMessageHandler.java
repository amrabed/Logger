package org.magnum.logger.communication.messaging;

import org.magnum.logger.Encryptor;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

public class SentMessageHandler extends ContentObserver
{
	final static String TAG = "MESSAGE";
	Context context;

	public SentMessageHandler(Context context, Handler handler)
	{
		super(handler);
		this.context = context;
	}

	@Override
	public void onChange(boolean selfChange)
	{
		super.onChange(selfChange);
		long time = System.currentTimeMillis();
		try
		{
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/sent/"), null, null, null, null);
			cursor.moveToNext();
			time = cursor.getLong(cursor.getColumnIndex("date"));
			String id = Encryptor.encryptPhoneNumber(cursor.getString(cursor.getColumnIndex("address")), context);
			if ((PreferenceManager.getDefaultSharedPreferences(context).getLong(TAG + "time", 0) != time) || (!PreferenceManager.getDefaultSharedPreferences(context).getString(TAG + "address", "").equals(id)))
			{
				// TODO Temporary Work-around for repeated call
				new MessageTable(context).insert(time, id, 0);
				Log.d(TAG, "Message sent to " + id + " at " + time);
				PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(TAG + "time", time).commit();
				PreferenceManager.getDefaultSharedPreferences(context).edit().putString(TAG + "address", id).commit();
			}
			cursor.close();
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}

	}
	// @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	// @Override
	// public void onChange(boolean selfChange, Uri uri)
	// {
	// super.onChange(selfChange, uri);
	// long time = System.currentTimeMillis();
	// try
	// {
	// Cursor cur = context.getContentResolver().query(uri, null, null, null,
	// null);
	//
	// cur.moveToNext();
	// int type = cur.getInt(cur.getColumnIndex("type"));
	// if (type == 2)
	// {
	// time = cur.getLong(cur.getColumnIndex("date"));
	// String id =
	// Encryptor.encryptPhoneNumber(cur.getString(cur.getColumnIndex("address")),
	// context);
	// new MessageTable(context).insert(time, id, 0);
	//
	// Log.d(TAG, "Message sent to " + id + " at " + time);
	// }
	// cur.close();
	// }
	// catch (Exception e)
	// {
	// Log.e(TAG, e.toString());
	// }
	// }

}
