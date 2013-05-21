package org.magnum.logger.connectivity.usb;

import org.magnum.logger.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UsbTable extends Database
{

	private static final String TABLE_NAME = "Bluetooth";
	private static final String COLUMN_NAMES[] = { "Time", "Name", "Class",  "Change" };

	public UsbTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  Name TEXT NOT NULL, Class TEXT NOT NULL, Change TEXT NOT NULL )");
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
