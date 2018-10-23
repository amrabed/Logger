package org.magnum.logger.connectivity.mobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import org.magnum.logger.Encryptor;
import org.magnum.logger.device.modes.ModeHandler;

public class MobileHandler extends PhoneStateListener
{

	private static final String TAG = ModeHandler.class.getCanonicalName();

	private Context context;

	public MobileHandler(Context context)
	{
		this.context = context;
	}

	@Override
	public void onCellLocationChanged(CellLocation location)
	{
		long time = System.currentTimeMillis();
		try
		{
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			int type = info.getSubtype();
			Integer id;

			if (type == TelephonyManager.NETWORK_TYPE_CDMA)
			{
				CdmaCellLocation cdma = (CdmaCellLocation) location;
				id = cdma.getBaseStationId();
			}
			else
			{
				GsmCellLocation gsm = (GsmCellLocation) location;
				id = gsm.getCid();
			}
			if (id != PreferenceManager.getDefaultSharedPreferences(context).getInt(TAG, -2))
			{
				new MobileTable(context).insert(time, Encryptor.encrypt(id.toString(), context), info.getSubtypeName());
				PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(TAG, id).commit();
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}
}
