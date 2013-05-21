package org.magnum.logger.user.applications;

import org.magnum.logger.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ApplicationTable extends Database
{

	private static final String TABLE_NAME = "Applications";
	private static final String COLUMN_NAMES[] = { "Time", "AppID", "AccessType" };

	public ApplicationTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  AppID TEXT NOT NULL, AccessType TEXT NOT NULL)");
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
