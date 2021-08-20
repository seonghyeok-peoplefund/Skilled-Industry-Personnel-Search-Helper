package com.ray.personnel.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import com.ray.personnel.data.Company
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CompanyDao {

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

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY salaryRookey ASC")
    fun getAllBySalaryAsc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY salaryRookey DESC")
    fun getAllBySalaryDesc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY scaleFourth - (3 * scaleNormal) DESC")
    fun getAllByPercentAsc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE sortType = :sortType ORDER BY scaleFourth - (3 * scaleNormal) ASC")
    fun getAllByPercentDesc(sortType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY distance ASC")
    fun getLikedByDistanceAsc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY distance DESC")
    fun getLikedByDistanceDesc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY salaryRookey ASC")
    fun getLikedBySalaryAsc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY salaryRookey DESC")
    fun getLikedBySalaryDesc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY scaleFourth - (3 * scaleNormal) DESC")
    fun getLikedByPercentAsc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE isLiked = 1 ORDER BY scaleFourth - (3 * scaleNormal) ASC")
    fun getLikedByPercentDesc(): Single<List<Company>>
}