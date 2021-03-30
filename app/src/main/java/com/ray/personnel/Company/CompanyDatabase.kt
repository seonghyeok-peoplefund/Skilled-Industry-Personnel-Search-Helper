package com.ray.personnel.Company

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable


@Database(entities = [Company::class], version = 4)
@TypeConverters(LocationConverter::class)
abstract class CompanyDatabase : RoomDatabase() {
    abstract fun companyDao(): CompanyDao

    companion object{
        private val instance: CompanyDatabase? = null
        fun getInstance(context: Context) = instance ?: buildDataBase(context)

        private fun buildDataBase(context: Context): CompanyDatabase {
            return Room.databaseBuilder(context.applicationContext, CompanyDatabase::class.java, "company_db")
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).build()
        }
    }



}

/*

    abstract fun companyDao(): CompanyDao

    companion object {

        private var instance: CompanyDatabase? = null

        @Synchronized
        fun getInstance(context: Context): CompanyDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                    CompanyDatabase::class.java, "company_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }
    }




    abstract fun companyDao(): CompanyDao

 */