package com.ray.personnel.domain.parser

import android.util.Log
import com.ray.personnel.Constants
import com.ray.personnel.data.Company
import com.ray.personnel.data.GeoLocation
import com.ray.personnel.data.Location
import com.ray.personnel.domain.LocationManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException

object CompanyDetailParser {
    private const val TAG = "CompanyDetailParser"

    fun initDetail(
        company: Company,
        currentGeoLocation: GeoLocation,
        loginToken: String,
        onSuccess: (Consumer<in Company>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return Observable
            .fromCallable {
                company.initJobDetail(currentGeoLocation)
                    ?.initCompanyDetail(loginToken)
                    ?.initMilitaryDetail()
                    ?: throw IOException("정상적으로 company initialize 작업이 진행이 되지 않음.")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    private fun Company.initJobDetail(currentGeoLocation: GeoLocation): Company? {
        val jobDoc: JSONObject = JSONObject(
            try {
                Jsoup.connect(Constants.WANTED_INFORMATION + jobId)
                    .ignoreContentType(true)
                    .execute()
                    .body()
            } catch (e: HttpStatusException) {
                Log.e(TAG, "인터넷 연결이 올바르지 않습니다", e)
                return null
            }
        )
        jobDoc.optJSONObject("job")
            ?.optJSONObject("detail")
            .let { json ->
                intro = json?.optString("intro") ?: return null
                mainTasks = json.optString("main_tasks") ?: return null
                requirements = json.optString("requirements") ?: return null
                preferred = json.optString("preferred_points") ?: return null
                benefits = json.optString("benefits") ?: return null
            }
        jobDoc.optJSONObject("job")
            ?.optJSONObject("address")
            .let { json ->
                location = Location(
                    location = json?.optString("country") ?: return null,
                    fullLocation = json.optString("full_location") ?: return null,
                    geoLocation = GeoLocation(
                        json.optJSONObject("geo_location")
                            ?.optJSONObject("location")
                            ?.optDouble("lat") ?: return null,
                        json.optJSONObject("geo_location")
                            ?.optJSONObject("location")
                            ?.optDouble("lng") ?: return null
                    )
                )
            }
        distance = LocationManager.getDistance(
            location!!.geoLocation,
            GeoLocation(currentGeoLocation.latitude, currentGeoLocation.longitude)
            // chain을 이용하지 않았기에 인자가 두개여도 한줄로 쓴다.
        )
        intro = intro.replaceAfter(".", "")
            .replaceBeforeLast("\n", "")
            .replace(Regex(".+\\?"), "")
            .replace(Regex("【[^】]*】"), "")
            .replace(Regex("\\[[^\\]]*\\]"), "")
            .trim()
        companyId = jobDoc.optJSONObject("job")
            ?.optJSONObject("company")
            ?.optInt("id")
            ?.toString()
            ?: return null
        return this
    }

    private fun Company.initCompanyDetail(loginToken: String): Company? {
        val companyDoc: JSONObject = JSONObject(
            try {
                Jsoup.connect("https://www.wanted.co.kr/api/v4/companies/$companyId/salary?period=1")
                    .cookie(Constants.TOKEN, loginToken)
                    .ignoreContentType(true)
                    .execute()
                    .body()
            } catch (e: HttpStatusException) {
                Log.e(TAG, "Wanted에서 정보를 제공하지 않음.", e)
                return null
            }
        )
        val length = companyDoc.optJSONArray("employee_histories")
            ?.length()
            ?: return null
        if (length > 0) {
            scale = companyDoc.optJSONArray("employee_histories")
                ?.optJSONObject(length - 1)
                ?.optInt("prsn_value")
                ?: return null
            scaleDate = companyDoc.optJSONArray("employee_histories")
                ?.optJSONObject(length - 1)
                ?.optString("base_ym")
                ?: return null
            salaryNormal = companyDoc.optJSONObject("salary")
                ?.optString("formatted_avg_salary")
                ?.replace("[^0-9]".toRegex(), "")
                ?.toInt()
                ?: return null
            salaryRookey = companyDoc.optJSONObject("salary")
                ?.optString("formatted_rookey_salary")
                ?.replace("[^0-9]".toRegex(), "")
                ?.toInt()
                ?: return null
        } else {
            return null
        }
        return this
    }

    private fun Company.initMilitaryDetail(): Company? {
        val militaryElement: Element? = try {
            Jsoup.connect(Constants.MILITARY_SEARCH + militaryUrl)
                .ignoreContentType(true)
                .get()
                .body()
        } catch (e: HttpStatusException) {
            Log.e(TAG, "Wanted에서 정보를 제공하지 않음.", e)
            return null
        }
        val data = militaryElement
            ?.select("table.table_row")?.get(1)
            ?.select("tr")?.get(4)
            ?.select("td")
            ?: return null
        scaleNormal = data[0].text().filter { it.isDigit() }.toInt()
        scaleFourth = data[1].text().filter { it.isDigit() }.toInt()
        return this
    }
}