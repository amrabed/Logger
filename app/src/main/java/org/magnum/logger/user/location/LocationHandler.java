package org.magnum.logger.user.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocationHandler implements LocationListener
{

	private static final String TAG = LocationHandler.class.getCanonicalName();
	private Context context;

	public LocationHandler(Context context)
	{
		this.context = context;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		long time = System.currentTimeMillis();
		try
		{
			// LocationManager locationManager = (LocationManager)
			// context.getSystemService(Context.LOCATION_SERVICE);
			// Location lastLocation =
			// locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			float[] distances = getDistances(location);
			new LocationTable(context).insert(time, distances[0], distances[1], distances[2]);
			Log.d(TAG, "Location changed at " + time);
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}

	}

	private float[] getDistances(Location location)
	{
		float[] distances = new float[3];
		for (int i = 0; i < 3; i++)
		{
			final String lng = "Lng" + (i + 1);
			final String lat = "Lat" + (i + 1);
			// TODO: handle the case when the user didn't select 3 points
			double longitude = PreferenceManager.getDefaultSharedPreferences(context).getFloat(lng, 0);
			double latitude = PreferenceManager.getDefaultSharedPreferences(context).getFloat(lat, 0);
			final Location l = new Location(LocationManager.NETWORK_PROVIDER);
			l.setLatitude(latitude);
			l.setLongitude(longitude);
			distances[i] = location.distanceTo(l) / PreferenceManager.getDefaultSharedPreferences(context).getFloat("scale", 1);
		}
		return distances;
	}



	@Override
	public void onProviderEnabled(String provider)
	{
		// Implementing method from interface
	}

	@Override
	public void onProviderDisabled(String s)
	{
		// Implementing method from interface
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// Implementing method from interface
	}

}
