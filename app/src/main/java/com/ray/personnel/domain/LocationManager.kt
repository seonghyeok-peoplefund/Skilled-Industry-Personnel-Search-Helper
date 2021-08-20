package com.ray.personnel.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.ray.personnel.data.GeoLocation
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.roundToInt

object LocationManager {
    fun getDistance(locationA: GeoLocation, locationB: GeoLocation): Int {
        val radius = 6371e3
        val latitudeRadiusA = locationA.latitude * Math.PI / 180
        val latitudeRadiusB = locationB.latitude * Math.PI / 180
        val longitudeRadiusA = locationA.longitude * Math.PI / 180
        val longitudeRadiusB = locationB.longitude * Math.PI / 180
        val latitudeRadiusDelta = latitudeRadiusB - latitudeRadiusA
        val longitudeRadiusDelta = longitudeRadiusB - longitudeRadiusA
        val angle = sin(latitudeRadiusDelta / 2) *
                sin(latitudeRadiusDelta / 2) +
                cos(latitudeRadiusA) *
                cos(latitudeRadiusB) *
                sin(longitudeRadiusDelta / 2) *
                sin(longitudeRadiusDelta / 2)
        val angularDistance = 2 * atan2(sqrt(angle), sqrt(1 - angle))
        return (radius * angularDistance).roundToInt()
    }

    fun getLocation(onSuccess: Consumer<in Location>, onError: Consumer<in Throwable>): Disposable {
        return Single
            .fromCallable {
                getLocationWithCheckNetworkAndGPS(getApplication()) ?: throw IOException("위치를 얻지 못함.")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    private fun getLocationWithCheckNetworkAndGPS(ctx: Context): Location? {
        val locationManager = (ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkLocationEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        var networkLoacation: Location? = null
        if (isGpsEnabled) {
            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (isNetworkLocationEnabled) {
                    networkLoacation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }
                if (gpsLocation != null && networkLoacation != null) {
                    return if (gpsLocation.accuracy > networkLoacation.accuracy) networkLoacation else gpsLocation
                }
                if (gpsLocation != null) return gpsLocation
                if (networkLoacation != null) return networkLoacation
            }
        }
        return null
    }
}