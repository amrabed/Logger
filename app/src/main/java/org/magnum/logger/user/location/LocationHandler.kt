package org.magnum.logger.user.location

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import org.magnum.logger.Repository

class LocationHandler(private val context: Context) : LocationListener {
    override fun onLocationChanged(location: Location) {
        try {
            val time = System.currentTimeMillis()
            val distances = getDistances(location)
            Repository(context).logLocation(LocationRecord(time, distances[0], distances[1], distances[2]))
            Log.d(TAG, "Location changed at $time")
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    private fun getDistances(location: Location): FloatArray {
        val distances = FloatArray(3)
        for (i in 0..2) {
            // TODO: handle the case when the user didn't select 3 points
            val l = Location(LocationManager.NETWORK_PROVIDER)
            l.latitude = PreferenceManager.getDefaultSharedPreferences(context).getFloat("Lng" + (i + 1), 0f).toDouble()
            l.longitude = PreferenceManager.getDefaultSharedPreferences(context).getFloat("Lat" + (i + 1), 0f).toDouble()
            distances[i] = location.distanceTo(l) / PreferenceManager.getDefaultSharedPreferences(context).getFloat("scale", 1f)
        }
        return distances
    }

    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(s: String) {}

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

    companion object {
        private val TAG = LocationHandler::class.java.canonicalName
    }
}