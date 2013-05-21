package org.magnum.logger.user.files;

import org.magnum.logger.Encryptor;

import android.content.Context;
import android.os.FileObserver;
import android.util.Log;

public class FileHandler extends FileObserver
{

	final static String TAG = "FILE";
	Context context;
	String path;

	public FileHandler(Context context, String path)
	{
		super(path);
		this.path = path;
		this.context = context;
	}

	@Override
	public void onEvent(int event, String file)
	{
		synchronized (this)
		{
			long time = System.currentTimeMillis();
			try
			{
				String fullPath = path + "/" + file;
				new FileTable(context).insert(time, Encryptor.encrypt(fullPath, context), getEventString(event));
				Log.d(TAG, "File " + fullPath + " accessed at " + time);
			}
			catch (Exception e)
			{
				Log.e(TAG, e.toString());
			}
			finally
			{
				this.notifyAll();
			}
		}
	}

	private String getEventString(int event)
	{
		switch (event)
		{
		case FileObserver.ACCESS:
			return "ACCESS";
		case FileObserver.MODIFY:
			return "MODIFY";
		case FileObserver.ATTRIB:
			return "ATTRIB";
		case FileObserver.CLOSE_WRITE:
			return "CLOSE_WRITE";
		case FileObserver.CLOSE_NOWRITE:
			return "CLOSE_NOWRITE";
		case FileObserver.OPEN:
			return "OPEN";
		case FileObserver.MOVED_FROM:
			return "MOVED_FROM";
		case FileObserver.MOVED_TO:
			return "MOVED_TO";
		case FileObserver.CREATE:
			return "CREATE";
		case FileObserver.DELETE:
			return "DELETE";
		case FileObserver.DELETE_SELF:
			return "DELETE_SELF";
		case FileObserver.MOVE_SELF:
			return "MOVE_SELF";
		default:
			return "UNKNOWN";
		}
	}

}
