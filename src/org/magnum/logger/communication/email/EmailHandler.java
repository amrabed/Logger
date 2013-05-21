package org.magnum.logger.communication.email;

import org.magnum.logger.Encryptor;
import org.magnum.logger.communication.messaging.MessageTable;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.util.Log;

public class EmailHandler extends ContentObserver
{

	final static String TAG = "EMAIL";
	private Context context;

	public EmailHandler(Context context, Handler handler)
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
			Cursor cur = context.getContentResolver().query(Uri.withAppendedPath(Email.CONTENT_LOOKUP_URI, null), null, null, null, null);
			cur.moveToNext();
			// TODO Change to match email data
//			int type = cur.getInt(cur.getColumnIndex("type"));
//			if (type == 2)
			{
				time = cur.getLong(cur.getColumnIndex("date"));// getTimestampMillis();
				String id = Encryptor.encrypt(cur.getString(cur.getColumnIndex("address")), context);
				new MessageTable(context).insert(time, id, 0);

				Log.d(TAG, "Email  from/to " + id + " at " + time);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}

	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void onChange(boolean selfChange, Uri uri)
	{
		super.onChange(selfChange, uri);
		long time = System.currentTimeMillis();
		try
		{
			Cursor cur = context.getContentResolver().query(uri, null, null, null, null);

			cur.moveToNext();
			// TODO Change to match email data
//			int type = cur.getInt(cur.getColumnIndex("type"));
//			if (type == 2)
			{
				time = cur.getLong(cur.getColumnIndex("date"));
				String id = Encryptor.encrypt(cur.getString(cur.getColumnIndex("address")), context);
				new MessageTable(context).insert(time, id, 0);

				Log.d(TAG, "Email  from/to " + id + " at " + time);
			}

		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

}
