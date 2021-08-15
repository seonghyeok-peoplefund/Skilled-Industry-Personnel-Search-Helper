package com.ray.personnel.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import kotlin.math.roundToInt


class Location {

    lateinit var location: String
    lateinit var full_location: String
    lateinit var geo_location: GeoLocation


    class GeoLocation(val latitude: Double, val longitude: Double)

    companion object{
        fun getDistance(locationA: GeoLocation, locationB: GeoLocation): Int{
            val R = 6371e3; // metres
            val φ1 = locationA.latitude * Math.PI/180; // φ, λ in radians
            val φ2 = locationB.latitude * Math.PI/180;
            val Δφ = (locationB.latitude-locationA.latitude) * Math.PI/180;
            val Δλ = (locationB.longitude-locationA.longitude) * Math.PI/180;
            val a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
                    Math.cos(φ1) * Math.cos(φ2) *
                    Math.sin(Δλ/2) * Math.sin(Δλ/2);
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            return (R * c).roundToInt(); // in metres
        }
        fun getLocationWithCheckNetworkAndGPS(ctx: Context): android.location.Location? {
            val lm = (ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
            var isGpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            var isNetworkLocationEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            var networkLoacation: android.location.Location? = null
            if (isGpsEnabled) if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                var gpsLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (isNetworkLocationEnabled) networkLoacation =
                    lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (gpsLocation != null && networkLoacation != null) {
                    //smaller the number more accurate result will
                    return if (gpsLocation.accuracy > networkLoacation.getAccuracy()) networkLoacation
                    else gpsLocation
                } else {
                    if (gpsLocation != null) {
                        return gpsLocation
                    } else if (networkLoacation != null) {
                        return networkLoacation
                    }
                }
            }
            return null

        }
    }

}