package com.ray.personnel.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ray.personnel.domain.parser.NaverParser
import io.reactivex.Single
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Company constructor(var title: String) : Comparable<Company>, Parcelable {
    /**
     * state means 'how many does it load'
     * 0 : init only
     * 1 : loaded by @link com.ray.personnel.Model.Parser.WantedParser
     */
    @PrimaryKey(autoGenerate = true)
    var id = 0

    /**
     * state 1
     * These informations will be initialized
     * before @link com.ray.personnel.Activity.CompanyActivity.CompanyList.CompanyList
     * !! loaded from First Parser - Parser, WantedParser !!
     */
    @ColumnInfo
    var sortType: Int = 0
    @ColumnInfo
    lateinit var department: String
    @ColumnInfo
    lateinit var militaryUrl: String
    @ColumnInfo
    lateinit var jobId: String
    @ColumnInfo
    lateinit var thumbURL: String
    @ColumnInfo
    var isLiked = false

    /**
     * state 2
     * These informations will be initialized
     * before @link com.ray.personnel.Activity.Info
     * !! loaded from Second Parser - NaverParser !!
     */
    @ColumnInfo
    var intro = "~~~를 하는 회사입니다."
    @ColumnInfo
    var mainTasks = "업무는 ~~~를 합니다."
    @ColumnInfo
    var requirements = "기술은 ~~가 필요합니다."
    @ColumnInfo
    var preferred = "기술 ~~이 있으면 우대해줍니다."
    @ColumnInfo
    var benefits = "복지는 ~~가 있습니."
    @ColumnInfo
    var location: Location? = null
    @ColumnInfo
    var distance: Int = 0x7fffffff
    @ColumnInfo
    lateinit var companyId: String
    @ColumnInfo
    var salaryRookey: Int = 0
    @ColumnInfo
    var salaryNormal: Int = 0
    @ColumnInfo
    var scale: Int = 0
    @ColumnInfo
    var scaleDate: String = "0000"
    @ColumnInfo
    var scaleNormal: Int = 0
    @ColumnInfo
    var scaleFourth: Int = 0

    /**
     * after @link com.ray.personnel.Activity.Info
     * these are observable - callback
     * do not need to save in database
     */
    val observableNews: Single<List<News>> get() = NaverParser.build(title)
    @Ignore
    var news: List<News>? = null

    override fun compareTo(company: Company): Int {
        return title.compareTo(company.title)
    }
}