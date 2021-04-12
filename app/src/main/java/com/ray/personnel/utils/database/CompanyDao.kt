package com.ray.personnel.utils.database

import androidx.room.*
import com.ray.personnel.company.Company
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CompanyDao{

    @Query("SELECT * FROM company")
    fun getAll(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType")
    fun getAllCompanies(sortType: Int): Single<List<Company>>

    @Update
    fun updateAll(companies: List<Company>): Completable

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY distance ASC")
    fun getAllByDistanceAsc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY distance DESC")
    fun getAllByDistanceDesc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY salary_rookey ASC")
    fun getAllBySalaryAsc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY salary_rookey DESC")
    fun getAllBySalaryDesc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY isLiked ASC")
    fun getAllByLikeAsc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY isLiked DESC")
    fun getAllByLikeDesc(sortType: Int): Single<List<Company>>

    @Query("SELECT COUNT(*) FROM company WHERE sortType = :sortType")
    fun getSize(sortType: Int): Single<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBackground(company: Company)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(company: Company): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(companies: List<Company>): Completable

    @Update
    fun update(company: Company): Completable

    @Delete
    fun delete(company: Company): Completable

    @Query("SELECT * FROM company WHERE title = :title AND department = :department")
    fun getCompany(title: String, department: String): Single<Company?>

    @Query("DELETE FROM company")
    fun deleteAll(): Completable
}