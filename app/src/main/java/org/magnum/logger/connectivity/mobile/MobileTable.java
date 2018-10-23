package org.magnum.logger.connectivity.mobile;

import org.magnum.logger.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class MobileTable extends Database
{

	private static final String TABLE_NAME = "Mobile";
	private static final String COLUMN_NAMES[] = { "Time", "CellID", "Type" };

	public MobileTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  CellID INTEGER NOT NULL, Type TEXT )");
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
