package com.ray.personnel

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.GsonBuilder
import com.ray.personnel.company.Location
import java.io.File
import java.io.FileWriter


object Global : Application() {
    val gson = GsonBuilder().create()

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

    private const val PERMISSIONS_REQUEST_CODE = 100
    fun requestPermission(activity: Activity, vararg permissions: String): ArrayList<String> {
        val result = ArrayList<String>()
        var required = false
        permissions.forEach{ permission ->
            val hasPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
            if(!hasPermission){
                //거부한 적이 있다.
                if(!required) {
                    val denied_already = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
                    if (denied_already) {
                        Toast.makeText(activity, "이 기능이 필요합니다~~~ ㅎㅎ", Toast.LENGTH_LONG).show()
                        ActivityCompat.requestPermissions(activity, permissions, PERMISSIONS_REQUEST_CODE)
                    } else {
                        ActivityCompat.requestPermissions(activity, permissions, PERMISSIONS_REQUEST_CODE)
                    }
                    required = true
                }
            } else{
                //거부한적 없다.
                result.add(permission)
            }
        }
        return result //이미 허락된 것들 리스트를 리턴함.
    }


}