package org.magnum.logger.connectivity.devices;

import org.magnum.logger.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DeviceTable extends Database
{

	private static final String TABLE_NAME = "Devices";
	private static final String COLUMN_NAMES[] = { "Time", "DeviceID", "Change" };

	public DeviceTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  DeviceID TEXT NOT NULL, Change TEXT)");
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
