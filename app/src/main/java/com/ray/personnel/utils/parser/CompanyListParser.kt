package com.ray.personnel.utils.parser

import com.ray.personnel.company.Company
import org.json.JSONObject
import org.jsoup.Jsoup
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.io.IOException

object CompanyListParser : Publisher<Company>{

    const val MAX_SEARCH_COUNT = 100
    const val MAX_PRESUMED_COUNT = 333
    const val NO_PROGRESS = 0
    const val PARSING_WANTED = NO_PROGRESS + 1
    const val CHECKING_MILITARY = PARSING_WANTED + 1
    const val SEARCH_FINISHED = CHECKING_MILITARY + 1
    var sortType = -1

    private val wanted_url: String
        get() = "https://www.wanted.co.kr/api/v4/jobs?country=kr&locations=all&years=-1&limit=$MAX_SEARCH_COUNT&offset=$itemCount&job_sort=job.latest_order&tag_type_id=$sortType"
    private var progress = NO_PROGRESS
    private var jsonCompany: JSONObject? = null
    private var itemCount: Int = 0

    /**
     * step 1 - init Parser, parse military information
     * step 2 - wanted search by MAX_SEARCH_COUNT
     * step 3 - if isMilitary, add company
     * return progress and repeat step 2 - step 3.
     */
    override fun subscribe(s: Subscriber<in Company>) {
        val time_currnet: Long = System.currentTimeMillis()
        when(progress){
            NO_PROGRESS -> {
                progress++
                MilitaryParser.init()
            }
            PARSING_WANTED -> {
                jsonCompany = JSONObject(Jsoup.connect(wanted_url).ignoreContentType(true).execute().body())
                progress++
            }
            CHECKING_MILITARY -> {
                var i = 0
                while (i < MAX_SEARCH_COUNT) {
                    if (jsonCompany!!.isNull("data")) break
                    if (jsonCompany!!.getJSONArray("data").isNull(i)) break
                    val name = jsonCompany!!.getJSONArray("data").getJSONObject(i).getJSONObject("company")["name"].toString().replace("\\([^\\)]*\\)".toRegex(), "")
                    MilitaryParser.getMilitaryCompany(name)?.also{ c ->
                        val company = c.copy()
                        company.military_url = c.military_url
                        company.thumbURL = jsonCompany!!.getJSONArray("data").getJSONObject(i).getJSONObject("title_img")["origin"].toString()
                        company.department = jsonCompany!!.getJSONArray("data").getJSONObject(i).getString("position")
                        company.job_id = jsonCompany!!.getJSONArray("data").getJSONObject(i).getString("id")
                        company.sortType = sortType
                        s.onNext(company)
                    }
                    i ++
                }
                itemCount += i
                if(isParsingFinished()) progress++
                else progress--
            }
            SEARCH_FINISHED -> {
                jsonCompany = null
                progress++
                s.onComplete()
                println("wanted 개수 : $itemCount, military 개수 : "+MilitaryParser.sortedCompany.size)
            }
            else -> s.onError(IOException("어케한거여"))
        }
        if(progress <= SEARCH_FINISHED) {
            println("걸린 시간 : "+(System.currentTimeMillis() - time_currnet) / 1000.0+"초 걸림.")
            subscribe(s)
        }
        if(progress == SEARCH_FINISHED){
            progress = 0
        }
    }

    private fun isParsingFinished() = (jsonCompany != null && jsonCompany!!.getJSONObject("links")["next"].toString() == "null")

    fun isParsing() = progress > NO_PROGRESS && progress < SEARCH_FINISHED
    fun isNotParsing() = !isParsing()
}