package com.ray.personnel.company

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ray.personnel.utils.parser.NaverParser
import io.reactivex.Observable
import io.reactivex.Single


@Entity
data class Company constructor(var title: String) : Comparable<Company> {
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
    lateinit var military_url: String
    @ColumnInfo
    lateinit var job_id: String
    //
    //https://www.wanted.co.kr/wd/
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
    var main_tasks = "업무는 ~~~를 합니다."
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
    lateinit var company_id: String

    @ColumnInfo
    var salary_rookey: Int = 0
    @ColumnInfo
    var salary_normal: Int = 0
    @ColumnInfo
    var scale: Int = 0
    @ColumnInfo
    var scale_date: String = "0000"

    @ColumnInfo
    lateinit var scale_normal: String
    @ColumnInfo
    lateinit var scale_fourth: String
    /**
     * after @link com.ray.personnel.Activity.Info
     * these are observable - callback
     * do not need to save in database
     */
    val observableNews: Single<ArrayList<News>>
        get() = NaverParser.Builder.build(title)
    @Ignore
    var news: ArrayList<News>? = null

/*
    var salary: Int? = null
        get(){
            if(field == null){
                val arr = CompanyBasicInformationParser.Builder.build(title, business_number)
                scale = arr.get(0)
                salary = arr.get(1)
            }
            return field
        }

    var scale: Int? = null
        get(){
            if(field == null){
                val arr = CompanyBasicInformationParser.Builder.build(title, business_number)
                scale = arr.get(0)
                salary = arr.get(1)
            }
            return field
        }*/


    constructor(title: String, military_url: String): this(title) {
        this.military_url = military_url
    }

    override fun compareTo(company: Company): Int {
        return title.compareTo(company.title)
    }


}