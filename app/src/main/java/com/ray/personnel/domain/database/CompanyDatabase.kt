package com.ray.personnel.domain.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ray.personnel.Global
import com.ray.personnel.data.Company
import com.ray.personnel.data.Location
import com.ray.personnel.domain.parser.CompanyListParser


@Database(entities = [Company::class], version = 1)
@TypeConverters(LocationConverter::class)
abstract class CompanyDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    companion object{
        private var instance: CompanyDatabase? = null
        fun getInstance(context: Context): CompanyDatabase = instance
                ?: run {
            instance = Room.databaseBuilder(context.applicationContext, CompanyDatabase::class.java, "company_db_"+CompanyListParser.sortType)
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    }).build()
            instance!!
        }

    }
}

class LocationConverter {
    @TypeConverter
    fun jsonToLocation(value: String?): Location? {
        return value?.let { Gson().fromJson(value, Location::class.java) }
    }

    @TypeConverter
    fun locationToJson(location: Location?): String? {
        return location?.let { Gson().toJson(location) }
    }

    @TypeConverter
    fun jsonToList(value: String?): List<String>? {
        return value?.let { Gson().fromJson(value, object: TypeToken<List<String>>(){}.type) }
    }

    @TypeConverter
    fun listToJson(list: List<String>?): String? {
        return list?.let { Gson().toJson(list) }
    }
}