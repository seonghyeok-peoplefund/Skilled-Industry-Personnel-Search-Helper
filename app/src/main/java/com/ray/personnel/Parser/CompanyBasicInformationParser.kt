package com.ray.personnel.Parser

import com.ray.personnel.Company.News
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.Jsoup
import java.net.URLEncoder
import java.util.ArrayList
import java.util.concurrent.Callable

class CompanyBasicInformationParser {
    companion object Builder {
        fun build(name: String, num: Int): Observable<ArrayList<Int>> =
                Observable.fromCallable(parse(name, num)).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())

        private fun parse(name: String, num: Int): Callable<ArrayList<Int>> = Callable<ArrayList<Int>> {
            val jsonNews = JSONObject(Jsoup.connect("https://sh49eptmi2.execute-api.ap-northeast-2.amazonaws.com/v2/programmers/api/avgSalaryService")
                    .header("name", name)
                    .header("bsNo", num.toString())
                    .method(Connection.Method.POST).ignoreContentType(true).execute().body()).optJSONObject("body")
            val result = ArrayList<Int>()
            result.add(jsonNews.optInt("employee_count"))
            result.add(jsonNews.optString("salary").replace(",", "").replace("만원", "").toInt())
            return@Callable result

        }
    }
}