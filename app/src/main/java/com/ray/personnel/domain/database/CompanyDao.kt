package com.ray.personnel.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query
import com.ray.personnel.data.Company
import io.reactivex.Completable
import io.reactivex.Single

// 인터넷에서 가져온 내용
@Dao
interface CompanyDao {
    @Update
    fun update(companies: List<Company>): Completable

    @Update
    fun update(company: Company): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(company: Company): Completable

    @Query("SELECT * FROM company WHERE department_type = :departmentType ORDER BY distance ASC")
    fun getAllByDistanceAsc(departmentType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE department_type = :departmentType ORDER BY distance DESC")
    fun getAllByDistanceDesc(departmentType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE department_type = :departmentType ORDER BY salary_rookey ASC")
    fun getAllBySalaryAsc(departmentType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE department_type = :departmentType ORDER BY salary_rookey DESC")
    fun getAllBySalaryDesc(departmentType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE department_type = :departmentType ORDER BY employees_reserve_personnel - (3 * employees_active_personnel) DESC")
    fun getAllByPercentAsc(departmentType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE department_type = :departmentType ORDER BY employees_reserve_personnel - (3 * employees_active_personnel) ASC")
    fun getAllByPercentDesc(departmentType: Int): Single<List<Company>>

    @Query("SELECT * FROM company WHERE is_liked = 1 ORDER BY distance ASC")
    fun getLikedByDistanceAsc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE is_liked = 1 ORDER BY distance DESC")
    fun getLikedByDistanceDesc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE is_liked = 1 ORDER BY salary_rookey ASC")
    fun getLikedBySalaryAsc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE is_liked = 1 ORDER BY salary_rookey DESC")
    fun getLikedBySalaryDesc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE is_liked = 1 ORDER BY employees_reserve_personnel - (3 * employees_active_personnel) DESC")
    fun getLikedByPercentAsc(): Single<List<Company>>

    @Query("SELECT * FROM company WHERE is_liked = 1 ORDER BY employees_reserve_personnel - (3 * employees_active_personnel) ASC")
    fun getLikedByPercentDesc(): Single<List<Company>>
}