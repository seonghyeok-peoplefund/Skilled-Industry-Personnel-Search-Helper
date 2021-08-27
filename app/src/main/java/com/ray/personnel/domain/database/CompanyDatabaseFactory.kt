package com.ray.personnel.domain.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.ray.personnel.data.Company
import com.ray.personnel.data.Location

// 인터넷에서 가져온 내용
@Database(entities = [Company::class], version = 1)
@TypeConverters(LocationConverter::class)
abstract class CompanyDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao
    //main thread 에서 room db 접근시 IllegalStateException : Cannot access database on the main thread since it may potentially lock the UI
    // for a long periods of time.

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

             TODO("-> runBlocking 이용해야하나? 이건 바꾸기 전에 더 자세하게 공부해야하겠다.")
             */
            //아직 처리 못함
            return instance!!
            /*?: runBlocking {
                instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        CompanyDatabase::class.java,
                        "company_db_${CompanyListParser}.db"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                return instance!!
            }
            */

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