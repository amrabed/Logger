package org.magnum.logger.connectivity.wifi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.magnum.logger.Database;

public class WifiTable extends Database
{

	private static final String TABLE_NAME = "WiFi";
	private static final String[] COLUMN_NAMES = {"Time", "BSSID"};

	WifiTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  BSSID INTEGER NOT NULL )");
	}

	@Override
	public String[] getColumnNames()
	{
		return COLUMN_NAMES;
	}

	@Override
	public String getTableName()
	{
		return TABLE_NAME;
	}
}
