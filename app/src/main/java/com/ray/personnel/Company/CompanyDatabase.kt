package com.ray.personnel.Company

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.reflect.TypeToken
import com.ray.personnel.Activity.Global
import com.ray.personnel.Parser.CompanyListParser
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable


@Database(entities = [Company::class], version = 1)
@TypeConverters(LocationConverter::class)
abstract class CompanyDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    companion object{
        private var instance: CompanyDatabase? = null
        fun getInstance(context: Context): CompanyDatabase = instance ?: run {
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
        return value?.let { Global.gson.fromJson(value, Location::class.java) }
    }

    @TypeConverter
    fun locationToJson(location: Location?): String? {
        return location?.let { Global.gson.toJson(location) }
    }

    @TypeConverter
    fun jsonToList(value: String?): List<String>? {
        return value?.let { Global.gson.fromJson(value, object: TypeToken<List<String>>(){}.type) }
    }

    @TypeConverter
    fun listToJson(list: List<String>?): String? {
        return list?.let { Global.gson.toJson(list) }
    }
}