package com.ray.personnel.Company

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.internal.util.LinkedArrayList

@Dao
interface CompanyDao{
    @Query("SELECT * FROM company")
    fun getAll(): Flowable<List<Company>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(company: Company): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(companies: List<Company>): Completable

    @Update
    fun update(company: Company): Completable

    @Delete
    fun delete(company: Company): Completable

    @Query("SELECT * FROM company WHERE title = :title AND department = :department")
    fun getCompany(title: String, department: String): Observable<Company?>

    @Query("DELETE FROM company")
    fun deleteAll(): Completable
}