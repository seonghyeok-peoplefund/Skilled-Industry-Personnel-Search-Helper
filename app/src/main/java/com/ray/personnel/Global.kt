package com.ray.personnel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder
import com.ray.personnel.company.Location
import java.io.File
import java.io.FileWriter

object Global : Application() {
    val gson = GsonBuilder().create()
    val curLocation = Location.GeoLocation(37.49588155109891, 127.03185679685451)

    fun checkPermissionForCulture(context: Context?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context!!,
                            Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
            return if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                false
            } else ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    fun saveFile(text: String){
        try {
            val fw = FileWriter(File(Environment.getExternalStorageDirectory().toString()+"/log_temp.txt"))
            fw.use {
                fw.write(text)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}