package com.ray.personnel.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

//애초에 설계를 잘못한 듯 하다. 이로 인해 무분별하게 null을 사용하게 되었다. (데이터가 비어있다가 점점 채워지는 형식...)
@Parcelize
@Entity
data class Company(
    val title: String,

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo
    var departmentType: Int = 0,

    @ColumnInfo
    var department: String? = null,

    @ColumnInfo
    var militaryUrl: String? = null,

    @ColumnInfo
    var jobId: String? = null,

    @ColumnInfo
    var thumbURL: String? = null,

    @ColumnInfo
    var isLiked: Boolean = false,

    @ColumnInfo
    var intro: String = "~~~를 하는 회사입니다.",

    @ColumnInfo
    var mainTasks: String = "업무는 ~~~를 합니다.",

    @ColumnInfo
    var requirements: String = "기술은 ~~가 필요합니다.",

    @ColumnInfo
    var preferred: String = "기술 ~~이 있으면 우대해줍니다.",

    @ColumnInfo
    var benefits: String = "복지는 ~~가 있습니.",

    @ColumnInfo
    var location: Location? = null,

    @ColumnInfo
    var distance: Int = Int.MAX_VALUE,

    @ColumnInfo
    var companyId: String? = null,

    @ColumnInfo
    var salaryRookey: Int = 0,

    @ColumnInfo
    var salaryNormal: Int = 0,

    @ColumnInfo
    var scale: Int = 0,

    @ColumnInfo
    var scaleDate: String = "0000",

    @ColumnInfo
    var scaleNormal: Int = 0,

    @ColumnInfo
    var scaleFourth: Int = 0, //trailing comma
) : Comparable<Company>, Parcelable {
    @IgnoredOnParcel
    @Ignore
    var news: List<News>? = null

    override fun compareTo(other: Company): Int {
        return title.compareTo(other.title)
    }
}