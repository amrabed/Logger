package org.magnum.logger.user.files;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.magnum.logger.Database;

public class FileTable extends Database
{

	private static final String TABLE_NAME = "Files";
	private static final String[] COLUMN_NAMES = {"Time", "FileID", "AccessType"};

	FileTable(Context context)
	{
		super(context);
	}

	public static void create(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( Time INTEGER NOT NULL,  FileID INTEGER NOT NULL, AccessType INTEGER)");
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
