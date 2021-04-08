package com.ray.personnel.viewmodel.company.filter

import android.Manifest
import android.app.Application
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ray.personnel.Global
import com.ray.personnel.company.Company
import com.ray.personnel.company.Location
import com.ray.personnel.company.Location.Companion.getLocationWithCheckNetworkAndGPS
import com.ray.personnel.fragment.company.CompanyListFragment
import com.ray.personnel.utils.Constants
import com.ray.personnel.utils.PreferenceManager
import com.ray.personnel.utils.database.CompanyDatabase
import com.ray.personnel.utils.parser.CompanyListParser
import com.ray.personnel.viewmodel.FragmentChangeModelInterface
import de.timonknispel.ktloadingbutton.KTLoadingButton
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup

class CompanyFilterViewModel(application: Application): AndroidViewModel(application), FragmentChangeModelInterface {
    override var curFragment = MutableLiveData<Fragment>()
    override val permissionRequest = MutableLiveData<List<String>>()
    override val permissionResult = MutableLiveData<List<String>>()
    var latitude = MutableLiveData<String>(Global.curLocation.latitude.toString())
    var longitude = MutableLiveData<String>(Global.curLocation.longitude.toString())
    var warningColor = MutableLiveData<Int>()
    var warningText = MutableLiveData<String>()
    var find = false

    val progress_max = MutableLiveData<Int>(100)
    val progress_cur = MutableLiveData<Int>(0)

    fun useGPS(v: View){
        permissionRequest.value = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if(find)
        Single.fromCallable{getLocationWithCheckNetworkAndGPS(getApplication())}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                { loc ->
                    latitude.value = loc?.latitude.toString()
                    longitude.value = loc?.longitude.toString()
                    (v as KTLoadingButton).doResult(true)
                },
                {err ->
                    (v as KTLoadingButton).doResult(false)
                    err.printStackTrace()
                }
        )
        else (v as KTLoadingButton).reset()
    }

    fun getCompanyList(){
        listDisposable = Observable.fromPublisher(CompanyListParser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progress_max.value = 100; progress_cur.value = 0 }
                .subscribe(
                        { p ->
                            getCompanyDetail(p)
                            company_stack ++
                            progress_cur.value = progress_cur.value?.plus(1) },
                        { err -> progress_cur.value = 0; println("onError - $err") },
                        { progress_cur.value = progress_cur.value?.plus(1) })
    }


    //"\.(?=(((?!\]).)*\[)|[^\[\]]*$)"
    //\.(?=(((?!\]).)*\[)|[^\[\]]*$) <- Pattern.MULTILINE (?m)
    var company_stack = 0
    lateinit var listDisposable: Disposable
    fun doFilter(v: View){
        //v.isClickable = false
        CompanyDatabase.getInstance(getApplication()).companyDao().getSize(CompanyListParser.sortType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{ size ->
            //왜 여러번될까
            if(size == 0) getCompanyList()
            else updateDistance()
        }
    }
    fun updateDistance(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { companies ->
            companies.forEach{ company ->
                company.distance = Location.getDistance(company.location!!.geo_location, Location.GeoLocation(latitude.value!!.toDouble(), longitude.value!!.toDouble()))
            }
            CompanyDatabase.getInstance(getApplication()).companyDao().updateAll(companies).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                curFragment.value = CompanyListFragment()
            }
        }
    }
    fun getCompanyDetail(c: Company){
        Observable.fromCallable {
            val doc = JSONObject(Jsoup.connect(Constants.WANTED_INFORMATION + c.job_id).ignoreContentType(true).execute().body())
            doc.optJSONObject("job").optJSONObject("detail").let { json ->
                c.intro = json.optString("intro")
                c.main_tasks = json.optString("main_tasks")
                c.requirements = json.optString("requirements")
                c.preferred = json.optString("preferred_points")
                c.benefits = json.optString("benefits")
            }
            c.location = Location().apply {
                doc.optJSONObject("job").optJSONObject("address").let { json ->
                    location = json.optString("country")
                    full_location = json.optString("full_location")
                    geo_location = Location.GeoLocation(
                            json.optJSONObject("geo_location").optJSONObject("location")
                                    .optDouble("lat"),
                            json.optJSONObject("geo_location").optJSONObject("location")
                                    .optDouble("lng")
                    )
                }
            }
            c.distance = Location.getDistance(c.location!!.geo_location, Location.GeoLocation(latitude.value!!.toDouble(), longitude.value!!.toDouble()))
            // regex로 괄호 바깥 & 따옴표 바깥에 있는 . 검색, 그뒤에는 제거함. 이후
            //TODO : 알고리즘 바꿔야함.
            c.intro = c.intro.replaceAfter(".", "").replaceBeforeLast("\n", "").replace(Regex(".+\\?"), "").replace(Regex("【[^】]*】"), "").replace(Regex("\\[[^\\]]*\\]"), "").trim()
            c.company_id = doc.optJSONObject("job").optJSONObject("company").optInt("id").toString()
            PreferenceManager.getString(getApplication(), Constants.TOKEN).let{ token ->
                try {
                    val doc2 = JSONObject(Jsoup.connect("https://www.wanted.co.kr/api/v4/companies/"+c.company_id+"/salary?period=1").cookie(Constants.TOKEN, token).ignoreContentType(true).execute().body())
                    val length = doc2.optJSONArray("employee_histories").length()
                    if(length > 0) {
                        c.scale = doc2.optJSONArray("employee_histories").getJSONObject(length - 1).getInt("prsn_value")
                        c.scale_date = doc2.optJSONArray("employee_histories").getJSONObject(doc2.optJSONArray("employee_histories").length() - 1).getString("base_ym")
                        c.salary_normal = doc2.optJSONObject("salary").optString("formatted_avg_salary").replace("[^0-9]".toRegex(), "").toInt()
                        c.salary_rookey = doc2.optJSONObject("salary").optString("formatted_rookey_salary").replace("[^0-9]".toRegex(), "").toInt()
                    } else{
                        println(c.title+"에는 정보가 담겨져 있지 않음.")
                        println(doc2.toString())
                    }
                } catch (e: HttpStatusException) {
                    println(c.title+"에는 정보가 담겨져 있지 않음.")
                }
            }
            c
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{ company ->
                CompanyDatabase.getInstance(getApplication()).companyDao().insert(company)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            company_stack--
                            if (listDisposable.isDisposed && company_stack == 0) {
                                curFragment.value = CompanyListFragment()
                            }
                        }
            }

        // listDisposable이 마지막 onNext이후 onComplete를 너무 늦게 내버린다면 stack == 0이면서 disposed = false일 수도 있음.
    }
}