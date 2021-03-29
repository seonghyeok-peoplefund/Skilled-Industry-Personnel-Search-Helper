package com.ray.personnel.Company

import android.net.Uri
import com.ray.personnel.Parser.CompanyBasicInformationParser
import com.ray.personnel.Parser.NaverParser
import io.reactivex.Observable
import java.net.MalformedURLException
import java.net.URI
import java.net.URL


//primary constructor is used for only comparing
class Company constructor(var title: String) : Comparable<Company> {
    /**
     * These informations will be initialized
     * before @link com.ray.personnel.Activity.CompanyActivity.CompanyList.CompanyList
     * !! loaded from First Parser - Parser, WantedParser !!
     */
    lateinit var department: String
    lateinit var military_url: String
    lateinit var recruit_url: String
    lateinit var thumbURL: URL

    /**
     * These informations will be initialized
     * before @link com.ray.personnel.Activity.Info
     * !! loaded from Second Parser - NaverParser !!
     */
    var description = "OOO 회사는 ~~~를 하는 회사입니다."

    /**
     * after @link com.ray.personnel.Activity.Info
     * these are observable - callback
     */
    val observableNews: Observable<ArrayList<News>>
        get() = NaverParser.Builder.build(title)
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