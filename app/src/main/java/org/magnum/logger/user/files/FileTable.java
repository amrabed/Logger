package org.magnum.logger.user.files;

import org.magnum.logger.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class FileTable extends Database
{

	private static final String TABLE_NAME = "Files";
	private static final String COLUMN_NAMES[] = { "Time", "FileID", "AccessType" };

	public FileTable(Context context)
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
