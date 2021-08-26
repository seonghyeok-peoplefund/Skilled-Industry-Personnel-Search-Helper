package com.ray.personnel.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GeoLocation(
    val latitude: Double,
    val longitude: Double
) : Parcelable