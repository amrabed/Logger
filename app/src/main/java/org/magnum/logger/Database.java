package org.magnum.logger;

import java.util.ArrayList;
import java.util.List;

import org.magnum.logger.communication.calls.CallTable;
import org.magnum.logger.communication.email.EmailTable;
import org.magnum.logger.communication.messaging.MessageTable;
import org.magnum.logger.connectivity.bluetooth.BluetoothTable;
import org.magnum.logger.connectivity.devices.DeviceTable;
import org.magnum.logger.connectivity.mobile.MobileTable;
import org.magnum.logger.connectivity.wifi.WifiTable;
import org.magnum.logger.device.modes.ModeTable;
import org.magnum.logger.device.power.PowerTable;
import org.magnum.logger.user.applications.ApplicationTable;
import org.magnum.logger.user.browsing.BrowserTable;
import org.magnum.logger.user.files.FileTable;
import org.magnum.logger.user.location.LocationTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class Database extends SQLiteOpenHelper
{

	private static final int VERSION = 1;
	public static final String NAME = "log.db";

	public Database(Context context)
	{
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		ApplicationTable.create(db);
		BluetoothTable.create(db);
		BrowserTable.create(db);
		CallTable.create(db);
		EmailTable.create(db);
		DeviceTable.create(db);
		//EventTable.create(db);
		FileTable.create(db);
		LocationTable.create(db);
		MobileTable.create(db);
		ModeTable.create(db);
		PowerTable.create(db);
		MessageTable.create(db);
		WifiTable.create(db);
	}

//	public void create(SQLiteDatabase db)
//	{
//		String statement = "CREATE TABLE IF NOT EXISTS " + getTableName() + " ( ";
//		for (String column : getColumnNames())
//		{
//			statement += column + " INTEGER NOT NULL, ";
//		}
//		statement += "\b\b )";
//		db.execSQL(statement);
//	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		final String TABLE_NAMES[] = { "Applications", "Bluetooth", "Browser", "Calls", "Devices", "Events", "Files", "Location", "Mobile", "Modes", "Power", "WiFi" };
		for (int i = 0; i < TABLE_NAMES.length; i++)
		{
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMES[i]);
		}
		onCreate(db);
	}

	public long insert(Object... columns)
	{
		ContentValues values = new ContentValues();
		for (int i = 0; i < columns.length; i++)
		{
			values.put(getColumnNames()[i], columns[i].toString());
		}

		SQLiteDatabase db = this.getWritableDatabase();
		long i = db.insert(getTableName(), null, values);
		db.close();
		return i;
	}

	public abstract String[] getColumnNames();

	public abstract String getTableName();

	public List<Entry> getAllEntries(String tableName)
	{
		List<Entry> list = new ArrayList<Entry>();
		String selectQuery = "SELECT  * FROM " + tableName;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		if (cursor.moveToFirst())
		{
			do
			{
				Entry entry = new Entry();
				for (int i = 0; i < cursor.getColumnCount(); i++)
				{
					entry.add(cursor.getString(i));
				}
				list.add(entry);
			} while (cursor.moveToNext());
		}

		return list;
	}

	public int getCount(String tableName)
	{
		String countQuery = "SELECT  * FROM " + tableName;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		return cursor.getCount();
	}

	static class Entry extends ArrayList<String>
	{
		private static final long serialVersionUID = 1L;
	}
}
