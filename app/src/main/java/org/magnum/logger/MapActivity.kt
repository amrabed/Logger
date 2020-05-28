package org.magnum.logger

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapActivity : FragmentActivity(), OnMapClickListener {
    private var numberOfClicks = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        try {
            val fragment = SupportMapFragment.newInstance(options)
            supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, TAG).commit()
            displayDialog(this)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun displayDialog(listener: OnMapClickListener) {
        try {
            AlertDialog.Builder(this).setMessage("Please select 3 random points").setPositiveButton("OK") { _, _ ->
                val fragment = mapFragment
                fragment?.getMapAsync { map -> map.setOnMapClickListener(listener) }
            }.show()
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private val mapFragment get() = supportFragmentManager.findFragmentByTag(TAG) as SupportMapFragment?

    private val options: GoogleMapOptions
        get() {
            val options = GoogleMapOptions()
            try {
                val location = lastKnownLocation
                if (location != null) {
                    options.mapType(GoogleMap.MAP_TYPE_NORMAL).camera(CameraPosition.Builder().target(LatLng(location.latitude, location.longitude)).zoom(10f).build()).zoomControlsEnabled(true)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            return options
        }

    override fun onBackPressed() {
        if (numberOfClicks < 3) {
            displayDialog(this)
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        try {
            val last = lastKnownLocation
            if (last != null) {
                while (numberOfClicks < 3) {

                    // User did not select three points
                    // Select random points
                    val location = LatLng(last.latitude + Random(System.currentTimeMillis()).nextInt(10) - 5, last.longitude + Random(System.currentTimeMillis()).nextInt(10) - 5)
                    setPreferences(location)
                    numberOfClicks++
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private val lastKnownLocation: Location?
        get() {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 100)
            } else {
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            return null
        }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
//        }
//    }

    override fun onMapClick(location: LatLng) {
        try {
            numberOfClicks++
            val fragment = supportFragmentManager.findFragmentByTag(TAG) as SupportMapFragment?
            fragment?.getMapAsync { map -> map.addMarker(MarkerOptions().position(location)) }
            setPreferences(location)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun setPreferences(location: LatLng) {
        try {
            val lng = "Lng$numberOfClicks"
            val lat = "Lat$numberOfClicks"
            PreferenceManager.getDefaultSharedPreferences(this).edit().putFloat(lng, location.longitude.toFloat()).apply()
            PreferenceManager.getDefaultSharedPreferences(this).edit().putFloat(lat, location.latitude.toFloat()).apply()
            if (numberOfClicks >= 3) {
                val l3 = Location(LocationManager.NETWORK_PROVIDER)
                l3.latitude = location.latitude
                l3.longitude = location.longitude
                val l2 = Location(LocationManager.NETWORK_PROVIDER)
                l2.latitude = PreferenceManager.getDefaultSharedPreferences(this).getFloat("Lat" + 2, 0f).toDouble()
                l2.longitude = PreferenceManager.getDefaultSharedPreferences(this).getFloat("Lng" + 2, 0f).toDouble()
                PreferenceManager.getDefaultSharedPreferences(this).edit().putFloat("scale", l3.distanceTo(l2)).apply()
                finish()
                Toast.makeText(this, "Application is now running in background", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    companion object {
        private val TAG = MapActivity::class.java.canonicalName
    }
}