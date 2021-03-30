package com.ray.personnel.Company

import androidx.room.TypeConverter
import com.ray.personnel.Activity.Global
import kotlin.math.roundToInt

class Location {

    lateinit var location: String
    lateinit var full_location: String
    lateinit var geo_location: GeoLocation


    inner class GeoLocation(val latitude: Double, val longitude: Double)

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
    }
}
class LocationConverter {
    @TypeConverter
    fun fromTimestamp(value: String?): Location? {
        return value?.let { Global.gson.fromJson(value, Location::class.java) }
    }

    @TypeConverter
    fun dateToTimestamp(location: Location?): String? {
        return Global.gson.toJson(location)
    }
}