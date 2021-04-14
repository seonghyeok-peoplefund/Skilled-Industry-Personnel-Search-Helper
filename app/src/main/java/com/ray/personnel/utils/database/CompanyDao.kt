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

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY distance ASC")
    fun getAllByDistanceAsc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY distance DESC")
    fun getAllByDistanceDesc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY salary_rookey ASC")
    fun getAllBySalaryAsc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY salary_rookey DESC")
    fun getAllBySalaryDesc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY scale_fourth - (3 * scale_normal) DESC")
    fun getAllByPercentAsc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY scale_fourth - (3 * scale_normal) ASC")
    fun getAllByPercentDesc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY distance ASC")
    fun getLikedByDistanceAsc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY distance DESC")
    fun getLikedByDistanceDesc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY salary_rookey ASC")
    fun getLikedBySalaryAsc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY salary_rookey DESC")
    fun getLikedBySalaryDesc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY scale_fourth - (3 * scale_normal) DESC")
    fun getLikedByPercentAsc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY scale_fourth - (3 * scale_normal) ASC")
    fun getLikedByPercentDesc(): Single<List<Company>>

// @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY CASE WHEN scale_fourth / IF(scale_normal == 0, 1, scale_normal) DESC")
//@Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY COALESCE(scale_fourth / NULLIF(scale_normal, 0), scale_fourth * 2) DESC")
}