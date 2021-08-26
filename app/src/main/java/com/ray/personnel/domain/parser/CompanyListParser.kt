package com.ray.personnel.domain.parser

import com.ray.personnel.data.Company
import org.json.JSONObject
import org.jsoup.Jsoup
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import java.io.IOException

object CompanyListParser : Publisher<Company> {
    const val MAX_SEARCH_COUNT = 100
    const val NO_PROGRESS = 0
    const val PARSING_WANTED = NO_PROGRESS + 1
    const val CHECKING_MILITARY = PARSING_WANTED + 1
    const val SEARCH_FINISHED = CHECKING_MILITARY + 1
    const val END = SEARCH_FINISHED + 1

    var departmentType = -1
    private val wantedUrl: String //TODO("긴 URL 어떻게 줄일건가?")
        get() = "https://www.wanted.co.kr/api/v4/jobs?country=kr&locations=all&years=-1&limit=$MAX_SEARCH_COUNT&offset=$itemCount&job_sort=job.latest_order&tag_type_id=$departmentType"
    private var progress = NO_PROGRESS
    private var jsonCompany: JSONObject? = null
    var itemCount: Int = 0
        private set

    /**
     * step 1 - init Parser, parse military information
     * step 2 - wanted search by MAX_SEARCH_COUNT
     * step 3 - if isMilitary, add company
     * return progress and repeat step 2 - step 3.
     */
    override fun subscribe(subscriber: Subscriber<in Company>) {
        when (progress) {
            NO_PROGRESS -> {
                progress++
                try {
                    MilitaryParser.getMilitaryInformationByJson()
                } catch (e: IOException) {
                    subscriber.onError(e)
                    progress = 0
                }
            }
            PARSING_WANTED -> {
                jsonCompany = JSONObject(
                    Jsoup.connect(wantedUrl)
                        .ignoreContentType(true)
                        .execute()
                        .body()
                )
                progress++
            }
            CHECKING_MILITARY -> {
                var i = 0
                while (i < MAX_SEARCH_COUNT) {
                    if (jsonCompany!!.isNull("data")) break
                    if (jsonCompany!!.getJSONArray("data")
                            .isNull(i)
                    ) break
                    val name = jsonCompany!!.getJSONArray("data")
                        .getJSONObject(i)
                        .getJSONObject("company")["name"]
                        .toString()
                        .replace("\\([^\\)]*\\)".toRegex(), "")
                    MilitaryParser.getMilitaryCompany(name)?.also { c ->
                        val company = c.copy()
                        company.militaryUrl = c.militaryUrl
                        company.thumbURL = jsonCompany!!.getJSONArray("data")
                            .getJSONObject(i)
                            .getJSONObject("title_img")["origin"]
                            .toString()
                        company.department = jsonCompany!!.getJSONArray("data")
                            .getJSONObject(i)
                            .getString("position")
                        company.jobId = jsonCompany!!.getJSONArray("data")
                            .getJSONObject(i)
                            .getString("id")
                        company.departmentType = departmentType
                        subscriber.onNext(company)
                    }
                    i++
                }
                itemCount += i
                if (isParsingFinished()) progress++
                else progress--
            }
            SEARCH_FINISHED -> {
                jsonCompany = null
                progress++
                subscriber.onComplete()
            }
            else -> subscriber.onError(IOException("이 곳에는 진입할 수 없음."))
        }
        if (progress < END) {
            subscribe(subscriber)
        }
        if (progress == END) {
            progress = 0
        }
    }

    //당장은 내부에서만 사용되지만, 외부에서 사용해도 상관없는 내용이다. 우선은 private 붙여놓는게 좋다고 판단했다.
    private fun isParsingFinished(): Boolean {
        return (jsonCompany != null && jsonCompany!!.getJSONObject("links")["next"].toString() == "null")
    }

    //위랑 같은 경우지만, isNotParsing이 외부에서 사용되고 있기에 같은 쌍인 해당 메소드는 외부에서 사용되지 않을지라도 public해야 한다고 판단했다.
    fun isParsing() = progress in (NO_PROGRESS + 1) until SEARCH_FINISHED

    fun isNotParsing() = !isParsing()
}