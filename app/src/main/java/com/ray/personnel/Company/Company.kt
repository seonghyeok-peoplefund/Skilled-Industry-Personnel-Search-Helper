package com.ray.personnel.Company

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.ray.personnel.Activity.Global
import com.ray.personnel.Parser.NaverParser
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Jsoup


@Entity(primaryKeys = arrayOf("title", "department"))
class Company constructor(var title: String) : Comparable<Company> {
    /**
     * state means 'how many does it load'
     * 0 : init only
     * 1 : loaded by @link com.ray.personnel.Parser.WantedParser
     */
    @ColumnInfo
    var state = 0
    /**
     * state 1
     * These informations will be initialized
     * before @link com.ray.personnel.Activity.CompanyActivity.CompanyList.CompanyList
     * !! loaded from First Parser - Parser, WantedParser !!
     */
    @ColumnInfo
    lateinit var department: String
    @ColumnInfo
    lateinit var military_url: String
    @ColumnInfo
    lateinit var wanted_id: String
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
    lateinit var location: Location

    //TODO : 금액, 규모
    /**
     * after @link com.ray.personnel.Activity.Info
     * these are observable - callback
     * do not need to save in database
     */
    val observableNews: Observable<ArrayList<News>>
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


    fun getInformation(step: Int){
        // TODO : 스케쥴러 바꿔서 Queue만들고, 모든Company에 대해서 실행하게 할것, 그리고 클릭대상이 되면 빠르게 그거먼저 로딩할것.
        if(state == step) when(step){
            1 -> Observable.fromCallable{
                val doc = JSONObject(Jsoup.connect(WANTED_INFORMATION_URL + wanted_id).ignoreContentType(true).execute().body())
                doc.optJSONObject("job").optJSONObject("detail").let { json ->
                    intro = json.optString ("intro")
                    main_tasks = json.optString("main_tasks")
                    requirements = json.optString("requirements")
                    preferred = json.optString("preferred_points")
                    benefits = json.optString("benefits")
                }
                location = Location().apply{
                    doc.optJSONObject("job").optJSONObject("address").let { json ->
                        location = json.optString ("country")
                        full_location = json.optString("full_location")
                        geo_location = GeoLocation(
                                json.optJSONObject("geo_location").optJSONObject("location").optDouble("lat"),
                                json.optJSONObject("geo_location").optJSONObject("location").optDouble("lng"))
                    }
                }
                state ++
            }.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe()

        }
    }

    companion object{
        private const val WANTED_INFORMATION_URL = "https://www.wanted.co.kr/api/v4/jobs/"
    }
}