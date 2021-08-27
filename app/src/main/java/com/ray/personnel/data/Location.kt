package com.ray.personnel.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val location: String,
    val fullLocation: String,
    val geoLocation: GeoLocation
) : Parcelable