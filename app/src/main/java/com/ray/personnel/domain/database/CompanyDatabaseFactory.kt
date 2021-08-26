package com.ray.personnel.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ray.personnel.data.Company
import com.ray.personnel.data.Location
import com.ray.personnel.domain.parser.CompanyListParser

@Database(entities = [Company::class], version = 1)
@TypeConverters(LocationConverter::class)
abstract class CompanyDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao

    companion object {
        private var instance: CompanyDatabase? = null
        fun getInstance(context: Context): CompanyDatabase {
            /*
             방안 1 : @synchronized fun getInstance
             -> 생성작업을 이미 완료해 instance != null 일 때에도 synchronized가 작동함.
             방안 2 : instance ?: synchronized(this)
             방안 3 : Instance ?: synchronized(CompanyDatabase::class)
             -> 어짜피 companion object라서 작동은 같을 것 같다. 더 직관성을 높이기 위해 방법3이 좋을 것 같다.
             이대로 사용해도 좋으려나?
             */
            return instance ?: synchronized(this) {
                instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        CompanyDatabase::class.java,
                        "company_db_${CompanyListParser}.db"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                // Chained call formatting 참고. 호출의 시작과 닫는 괄호를 수직으로 정렬함.
                return instance!!
            }
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
}