package org.magnum.logger.user.location;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.magnum.logger.Database;

public class LocationTable extends Database
{

	private static final String TABLE_NAME = "Location";
	private static final String[] COLUMN_NAMES = {"Time", "Distance1", "Distance2", "Distance3"};

	LocationTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  Distance1 REAL NOT NULL, Distance2 REAL NOT NULL, Distance3 REAL NOT NULL )");
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
