package org.magnum.logger;

import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapClickListener
{

	private static int NumberOfClicks = 0;
	private static String TAG = "map";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try
		{

			// setContentView(R.layout.activity_map);

			// TODO: handle when not connected to internet
			SupportMapFragment fragment = SupportMapFragment.newInstance(getOptions());
			getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment, TAG).commit();
			displayDialog(this);

		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}

	}

	private void displayDialog(final OnMapClickListener listener)
	{
		try
		{
			new AlertDialog.Builder(this).setMessage("Please select 3 random points").setPositiveButton("OK", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					((SupportMapFragment) getSupportFragmentManager().findFragmentByTag(TAG)).getMap().setOnMapClickListener(listener);
				}
			}).show();
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}

	}

	private GoogleMapOptions getOptions()
	{
		GoogleMapOptions options = new GoogleMapOptions();
		try
		{
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			options.mapType(GoogleMap.MAP_TYPE_NORMAL).camera(new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(10).build()).zoomControlsEnabled(true);

		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
		return options;

	}

	@Override
	public void onBackPressed()
	{
		if (NumberOfClicks < 3)
		{
			displayDialog(this);
			return;
		}
		super.onBackPressed();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		try
		{
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location last = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			for (; NumberOfClicks < 3; NumberOfClicks++)
			{
				// User did not select three points
				// Select random points
				LatLng location = new LatLng(last.getLatitude() + new Random(System.currentTimeMillis()).nextInt(10) - 5, last.getLongitude() + new Random(System.currentTimeMillis()).nextInt(10) - 5);
				setPreferences(location);
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}
	}

	@Override
	public void onMapClick(LatLng location)
	{
		try
		{
			NumberOfClicks++;
			((SupportMapFragment) getSupportFragmentManager().findFragmentByTag(TAG)).getMap().addMarker(new MarkerOptions().position(location));
			setPreferences(location);
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}

	}

	private void setPreferences(LatLng location)
	{
		try
		{
			String lng = "Lng" + NumberOfClicks, lat = "Lat" + NumberOfClicks;
			PreferenceManager.getDefaultSharedPreferences(this).edit().putFloat(lng, (float) location.longitude).commit();
			PreferenceManager.getDefaultSharedPreferences(this).edit().putFloat(lat, (float) location.latitude).commit();
			if (NumberOfClicks >= 3)
			{
				Location l3 = new Location(LocationManager.NETWORK_PROVIDER);
				l3.setLatitude(location.latitude);
				l3.setLongitude(location.longitude);
				Location l2 = new Location(LocationManager.NETWORK_PROVIDER);
				l2.setLatitude(PreferenceManager.getDefaultSharedPreferences(this).getFloat("Lat" + 2, 0));
				l2.setLongitude(PreferenceManager.getDefaultSharedPreferences(this).getFloat("Lng" + 2, 0));

				PreferenceManager.getDefaultSharedPreferences(this).edit().putFloat("scale", l3.distanceTo(l2)).commit();

				finish();
				Toast.makeText(this, "Application is now running in background", Toast.LENGTH_LONG).show();
			}
		}
		catch (Exception e)
		{
			Log.e(TAG, e.toString());
		}

	}
}