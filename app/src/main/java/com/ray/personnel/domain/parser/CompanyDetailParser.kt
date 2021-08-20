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
        onSuccess: (Consumer<in Company>)
    ): Disposable {
        return Observable
            .fromCallable {
                if (initJobDetail(company, currentGeoLocation)) {
                    initCompanyDetail(company, loginToken)
                }
                initMilitaryDetail(company)
                company
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    private fun initJobDetail(company: Company, currentGeoLocation: GeoLocation): Boolean {
        val jobDoc: JSONObject
        try {
            jobDoc = JSONObject(
                Jsoup.connect(Constants.WANTED_INFORMATION + company.jobId)
                    .ignoreContentType(true)
                    .execute()
                    .body()
            )
        } catch (e: IOException) {
            Log.e(TAG, "인터넷 연결이 올바르지 않습니다", e)
            return false
        }
        jobDoc.optJSONObject("job")
            ?.optJSONObject("detail")
            .let { json ->
                company.intro = json?.optString("intro") ?: return false
                company.mainTasks = json.optString("main_tasks") ?: return false
                company.requirements = json.optString("requirements") ?: return false
                company.preferred = json.optString("preferred_points") ?: return false
                company.benefits = json.optString("benefits") ?: return false
            }
        company.location = Location().apply {
            jobDoc.optJSONObject("job")
                ?.optJSONObject("address")
                .let { json ->
                    location = json?.optString("country") ?: return false
                    fullLocation = json.optString("full_location") ?: return false
                    geoLocation = GeoLocation(
                        json.optJSONObject("geo_location")
                            ?.optJSONObject("location")
                            ?.optDouble("lat")
                            ?: return false,
                        json.optJSONObject("geo_location")
                            ?.optJSONObject("location")
                            ?.optDouble("lng")
                            ?: return false
                    )
                }
        }
        company.distance = LocationManager.getDistance(
            company.location!!.geoLocation,
            GeoLocation(currentGeoLocation.latitude, currentGeoLocation.longitude)
        )
        company.intro =
            company.intro
                .replaceAfter(".", "")
                .replaceBeforeLast("\n", "")
                .replace(Regex(".+\\?"), "")
                .replace(Regex("【[^】]*】"), "")
                .replace(Regex("\\[[^\\]]*\\]"), "")
                .trim()
        company.companyId = jobDoc
            .optJSONObject("job")
            ?.optJSONObject("company")
            ?.optInt("id")
            ?.toString()
            ?: return false
        return true
    }

    private fun initCompanyDetail(company: Company, loginToken: String): Boolean {
        val companyDoc: JSONObject
        try {
            companyDoc = JSONObject(
                Jsoup.connect("https://www.wanted.co.kr/api/v4/companies/${company.companyId}/salary?period=1")
                    .cookie(Constants.TOKEN, loginToken)
                    .ignoreContentType(true)
                    .execute()
                    .body()
            )
        } catch (e: HttpStatusException) {
            Log.e(TAG, "Wanted에서 정보를 제공하지 않음.", e)
            return false
        }
        val length = companyDoc.optJSONArray("employee_histories")
            ?.length()
            ?: return false
        if (length > 0) {
            company.scale = companyDoc.optJSONArray("employee_histories")
                ?.optJSONObject(length - 1)
                ?.optInt("prsn_value")
                ?: return false
            company.scaleDate = companyDoc.optJSONArray("employee_histories")
                ?.optJSONObject(length - 1)
                ?.optString("base_ym")
                ?: return false
            company.salaryNormal = companyDoc.optJSONObject("salary")
                ?.optString("formatted_avg_salary")
                ?.replace("[^0-9]".toRegex(), "")
                ?.toInt()
                ?: return false
            company.salaryRookey = companyDoc.optJSONObject("salary")
                ?.optString("formatted_rookey_salary")
                ?.replace("[^0-9]".toRegex(), "")
                ?.toInt()
                ?: return false
        } else {
            return false
        }
        return true
    }

    private fun initMilitaryDetail(company: Company): Boolean {
        val militaryDoc: Element
        try {
            militaryDoc = Jsoup.connect(Constants.MILITARY_SEARCH + company.militaryUrl)
                .ignoreContentType(true)
                .get()
                .body()
        } catch (e: HttpStatusException) {
            Log.e(TAG, "Wanted에서 정보를 제공하지 않음.", e)
            return false
        }
        val data = militaryDoc
            ?.select("table.table_row")?.get(1)
            ?.select("tr")?.get(4)
            ?.select("td")
            ?: return false
        company.scaleNormal = data[0].text().filter { it.isDigit() }.toInt()
        company.scaleFourth = data[1].text().filter { it.isDigit() }.toInt()
        return true
    }
}