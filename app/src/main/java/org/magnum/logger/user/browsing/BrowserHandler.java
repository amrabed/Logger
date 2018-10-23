package org.magnum.logger.user.browsing;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Browser;
import android.util.Log;

import org.magnum.logger.Encryptor;

public class BrowserHandler extends ContentObserver
{

	private static final String TAG = BrowserHandler.class.getCanonicalName();
	private final Context context;

	public BrowserHandler(Context context, Handler handler)
	{
		super(handler);
		this.context = context;
	}

	@Override
	public void onChange(boolean selfChange)
	{
		super.onChange(selfChange);
		try
		{
			Cursor cursor = context.getContentResolver().query(Browser.BOOKMARKS_URI, Browser.HISTORY_PROJECTION, null, null, null);
			if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0)
			{
				long time;
				String url;
				while (!cursor.isAfterLast())
				{

					time = cursor.getLong(Browser.HISTORY_PROJECTION_DATE_INDEX);
					url = cursor.getString(Browser.HISTORY_PROJECTION_URL_INDEX);
					int i = url.indexOf('/');
					int j = url.indexOf('/', i + 2);
					String domain;
					if(j != -1)
					{
						domain = url.substring(i + 2, j);
					}
					else
					{
						domain = url.substring(i+2);
					}

					if (time > PreferenceManager.getDefaultSharedPreferences(context).getLong(TAG, 0))
					{
						new BrowserTable(context).insert(time, Encryptor.encrypt(domain, context));
						PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(TAG, time).commit();
						Log.d(TAG, "URL " + domain + " visited at " + time);
					}
					cursor.moveToNext();
				}
				cursor.close();
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public boolean deliverSelfNotifications()
	{
		return false;
	}

}
