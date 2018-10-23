package org.magnum.logger.communication.email;

import org.magnum.logger.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class EmailTable extends Database
{
	private static final String TABLE_NAME = "Email";
	private static final String COLUMN_NAMES[] = { "Time", "Address"};// , "Received" };

	public EmailTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  Address INTEGER NOT NULL)");//, Received INTEGER NOT NULL )");
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
