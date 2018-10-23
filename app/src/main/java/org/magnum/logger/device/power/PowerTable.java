package org.magnum.logger.device.power;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.magnum.logger.Database;

public class PowerTable extends Database
{

	private static final String TABLE_NAME = "Power";
	private static final String[] COLUMN_NAMES = {"Time", "Status", "Level", "Plugged"};

	PowerTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  Status INTEGER NOT NULL, Level REAL NOT NULL, Plugged INTEGER NOT NULL )");
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
