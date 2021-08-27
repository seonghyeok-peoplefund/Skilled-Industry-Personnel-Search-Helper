package com.ray.personnel.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

//room db 예제 복붙해옴.
@Parcelize
@Entity
data class Company(
    val title: String,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "department_type")
    var departmentType: Int = 0,

    @ColumnInfo
    var department: String? = null,

    @ColumnInfo(name = "military_url")
    var militaryUrl: String? = null,

    @ColumnInfo(name = "job_id")
    var jobId: String? = null,

    @ColumnInfo(name = "thumb_url")
    var thumbUrl: String? = null,

    @ColumnInfo(name = "is_liked")
    var isLiked: Boolean = false,

    @ColumnInfo
    var intro: String = "~~~를 하는 회사입니다.",

    @ColumnInfo(name = "main_tasks")
    var mainTasks: String = "업무는 ~~~를 합니다.",

    @ColumnInfo
    var requirements: String = "기술은 ~~가 필요합니다.",

    @ColumnInfo
    var preferred: String = "기술 ~~이 있으면 우대해줍니다.",

    @ColumnInfo
    var welfare: String = "복지는 ~~가 있습니.",

    @ColumnInfo
    var location: Location? = null,

    @ColumnInfo
    var distance: Int = Int.MAX_VALUE,

    @ColumnInfo
    var companyId: String? = null,

    @ColumnInfo(name = "salary_rookey")
    var salaryRookey: Int = 0,

    @ColumnInfo(name = "salary_normal")
    var salaryNormal: Int = 0,

    @ColumnInfo
    var employees: Int = 0,

    @ColumnInfo(name = "employees_latest_date")
    var employeesLatestDate: String = "0000",

    @ColumnInfo(name = "employees_active_personnel")
    var employeesActivePersonnel: Int = 0,

    @ColumnInfo(name = "employees_reserve_personnel")
    var employeesReservePersonnel: Int = 0
) : Comparable<Company>, Parcelable {
    @IgnoredOnParcel
    @Ignore
    var news: List<News>? = null

    override fun compareTo(other: Company): Int {
        return title.compareTo(other.title)
    }
}