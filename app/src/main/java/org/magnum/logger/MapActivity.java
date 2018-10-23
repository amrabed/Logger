package org.magnum.logger;

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

import java.util.Random;

public class MapActivity extends FragmentActivity implements OnMapClickListener
{
	private static final String TAG = MapActivity.class.getCanonicalName();
	private int numberOfClicks = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		try
		{
			// TODO: Should handle when not connected to the internet
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
			if (locationManager != null)
			{
				Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				options.mapType(GoogleMap.MAP_TYPE_NORMAL).camera(new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(10).build()).zoomControlsEnabled(true);
			}
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
		if (numberOfClicks < 3)
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
			if (locationManager != null)
			{
				Location last = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				for (; numberOfClicks < 3; numberOfClicks++)
				{
					// User did not select three points
					// Select random points
					LatLng location = new LatLng(last.getLatitude() + new Random(System.currentTimeMillis()).nextInt(10) - 5, last.getLongitude() + new Random(System.currentTimeMillis()).nextInt(10) - 5);
					setPreferences(location);
				}
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
			numberOfClicks++;
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
			String lng = "Lng" + numberOfClicks;
			String lat = "Lat" + numberOfClicks;
			PreferenceManager.getDefaultSharedPreferences(this).edit().putFloat(lng, (float) location.longitude).commit();
			PreferenceManager.getDefaultSharedPreferences(this).edit().putFloat(lat, (float) location.latitude).commit();
			if (numberOfClicks >= 3)
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