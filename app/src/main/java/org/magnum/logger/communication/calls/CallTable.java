package org.magnum.logger.communication.calls;

import org.magnum.logger.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class CallTable extends Database
{
	private static final String TABLE_NAME = "Calls";
	private static final String COLUMN_NAMES[] = { "Time", "CallID", "Incoming", "Duration" };

	public CallTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  CallID INTEGER NOT NULL, Incoming INTEGER NOT NULL, Duration INTEGER )");
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

	public void setDuration(long callTime, long duration)
	{
		ContentValues dataToInsert = new ContentValues();                          
		dataToInsert.put("Duration", duration);
		String where = "Time=?";
		String[] whereArgs = new String[] {String.valueOf(callTime)};
		
		SQLiteDatabase db = getWritableDatabase();
		db.update(TABLE_NAME, dataToInsert, where, whereArgs);
		db.close();
	}
}
