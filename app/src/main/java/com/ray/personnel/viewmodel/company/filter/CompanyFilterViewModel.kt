package com.ray.personnel.viewmodel.company.filter

import android.Manifest
import android.app.Application
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ray.personnel.Global
import com.ray.personnel.company.Company
import com.ray.personnel.company.CompanyOccupation
import com.ray.personnel.company.Location
import com.ray.personnel.company.Location.Companion.getLocationWithCheckNetworkAndGPS
import com.ray.personnel.fragment.company.CompanyListFragment
import com.ray.personnel.utils.Constants
import com.ray.personnel.utils.Constants.Companion.MILITARY_SEARCH
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
import java.io.IOException

class CompanyFilterViewModel(application: Application): AndroidViewModel(application), FragmentChangeModelInterface {
    override var curFragment = MutableLiveData<Fragment>()
    override val permissionRequest = MutableLiveData<List<String>>()
    override val permissionResult = MutableLiveData<List<String>>()
    val jobs1 = MutableLiveData<List<String>>()
    val jobs2 = MutableLiveData<List<String>>()
    val jobs1value = MutableLiveData<Int>()
    val jobs2value = MutableLiveData<Int>()
    val jobs1listener = MutableLiveData<AdapterView.OnItemSelectedListener>()
    val jobs2listener = MutableLiveData<AdapterView.OnItemSelectedListener>()
    var latitude = MutableLiveData<String>()
    var longitude = MutableLiveData<String>()
    var warningColor = MutableLiveData<Int>()
    var warningText = MutableLiveData<String>()
    val progress_max = MutableLiveData<Int>(100)
    val progress_cur = MutableLiveData<Int>(0)

    lateinit var listDisposable: Disposable
    private var company_stack = 0
    var find = false

    private val beDisposed = ArrayList<Disposable>()

    fun useGPS(v: View){
        permissionRequest.value = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if(find) {
            beDisposed.add(Single.fromCallable { getLocationWithCheckNetworkAndGPS(getApplication()) }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    { loc ->
                        latitude.value = loc?.latitude.toString()
                        longitude.value = loc?.longitude.toString()
                        (v as KTLoadingButton).doResult(true)
                    },
                    { err ->
                        (v as KTLoadingButton).doResult(false)
                        err.printStackTrace()
                    }
            ))
        }
        else (v as KTLoadingButton).reset()
    }

    fun getCompanyList(){
        if(CompanyListParser.sortType == -1) {
            Toast.makeText(getApplication(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return;
        }
        if(latitude.value.isNullOrBlank() || longitude.value.isNullOrBlank()){
            Toast.makeText(getApplication(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            return;
        }
        if(CompanyListParser.isNotParsing()) {
            listDisposable = Observable.fromPublisher(CompanyListParser).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        progress_max.value = 100; progress_cur.value = 0
                        warningColor.value = (0xff shl 24) or 0x000000
                        warningText.value = "Parsing ??????.\n20???~60??? ?????? ??????????????????."
                    }
                    .subscribe(
                            { p ->
                                getCompanyDetail(p)
                                company_stack++
                                progress_cur.value = progress_cur.value?.plus(1)
                            },
                            { err -> progress_cur.value = 0;
                                Toast.makeText(getApplication(), err.toString(), Toast.LENGTH_SHORT).show(); },
                            {
                                warningText.value = "?????????"
                                progress_cur.value = 0
                                if(CompanyListParser.itemCount == 0) curFragment.value = CompanyListFragment()
                                })
        }
        else Toast.makeText(getApplication(), "?????? ????????? ??????????????????. ?????? ??????????????????.", Toast.LENGTH_SHORT).show()
    }


    //"\.(?=(((?!\]).)*\[)|[^\[\]]*$)"
    //\.(?=(((?!\]).)*\[)|[^\[\]]*$) <- Pattern.MULTILINE (?m)
    fun doFilter(v: View){
        beDisposed.add(CompanyDatabase.getInstance(getApplication()).companyDao().getSize(CompanyListParser.sortType).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{ size ->
            if(size == 0) getCompanyList()
            else updateDistance()
        })
    }
    fun updateDistance(){
        beDisposed.add(CompanyDatabase.getInstance(getApplication()).companyDao().getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { companies ->
            companies.forEach{ company ->
                company.distance = Location.getDistance(company.location!!.geo_location, Location.GeoLocation(latitude.value!!.toDouble(), longitude.value!!.toDouble()))
            }
            beDisposed.add(CompanyDatabase.getInstance(getApplication()).companyDao().updateAll(companies).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                curFragment.value = CompanyListFragment()
            })
        })
    }
    fun getCompanyDetail(c: Company){
        beDisposed.add(Observable.fromCallable {
            try{
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
                // regex??? ?????? ?????? & ????????? ????????? ?????? . ??????, ???????????? ?????????. ??????
                //TODO : ???????????? ????????????.
                c.intro = c.intro.replaceAfter(".", "").replaceBeforeLast("\n", "").replace(Regex(".+\\?"), "").replace(Regex("???[^???]*???"), "").replace(Regex("\\[[^\\]]*\\]"), "").trim()
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
                            println(c.title+"?????? ????????? ????????? ?????? ??????.")
                            println(doc2.toString())
                        }
                    } catch (e: HttpStatusException) {
                        println(c.title+"?????? ????????? ????????? ?????? ??????.")
                    }
                }

                val data = Jsoup.connect(MILITARY_SEARCH + c.military_url).ignoreContentType(true).get().body().select("table.table_row")[1].select("tr")[4].select("td")
                c.scale_normal = data[0].text().filter{ it.isDigit() }.toInt()
                c.scale_fourth = data[1].text().filter{ it.isDigit() }.toInt()
            } catch(e: IOException){
                Toast.makeText(getApplication(), "???????????? ???????????? ????????????.", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
            c
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{ company ->
            beDisposed.add(CompanyDatabase.getInstance(getApplication()).companyDao().insert(company)
                        .subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            company_stack--
                            if (listDisposable.isDisposed && company_stack == 0) {
                                curFragment.value = CompanyListFragment()
                            }
                        })
            })
        // listDisposable??? ????????? onNext?????? onComplete??? ?????? ?????? ??????????????? stack == 0????????? disposed = false??? ?????? ??????.
    }

    init{
        initSpinner()
        checkLogin()
    }

    fun checkLogin(){
        if(PreferenceManager.getString(getApplication(), Constants.TOKEN).isNullOrEmpty()) {
            warningColor.value = (0xff shl 24) or 0xff2020
            warningText.value = "?????? : Wanted???????????? ???????????? ????????????.\n?????? ??? ?????? ??????????????? ?????????????????????.\n???????????? ?????? ?????? Login ???????????? ?????? ?????? ??? ????????????."
        }
        else {
            warningColor.value = (0xff shl 24) or 0x000000
            warningText.value = "Wanted??? ???????????????????????????."
        }
    }
    fun initSpinner(){
        var position1 = PreferenceManager.getInt(getApplication(), Constants.JOB)
        var position2 = PreferenceManager.getInt(getApplication(), Constants.JOB_CLASSIFIED)
        if(position1 == -1) position1 = 0
        if(position2 == -1) position2 = 0

        jobs1.value = CompanyOccupation.occupation.keys.toList()
        jobs2.value = CompanyOccupation.occupation.values.toTypedArray()[position1].keys.toList()
        if(position1 > 0 && position2 > 0)
            CompanyListParser.sortType = CompanyOccupation.occupation.values.toTypedArray()[position1].values.toTypedArray()[position2]
        jobs1value.value = position1
        jobs2value.value = position2


        jobs1listener.value = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                jobs1value.value = position
                jobs2.value = CompanyOccupation.occupation.values.toTypedArray()[position].keys.toList()
                PreferenceManager.setInt(getApplication(), Constants.JOB, position)
                CompanyListParser.sortType = CompanyOccupation.occupation.values.toTypedArray()[position].values.toTypedArray()[0]

            }
        }
        jobs2listener.value = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                jobs2value.value = position
                CompanyListParser.sortType = CompanyOccupation.occupation.values.toTypedArray()[jobs1value.value!!].values.toTypedArray()[position]
                PreferenceManager.setInt(getApplication(), Constants.JOB_CLASSIFIED, position)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        for(disposed in beDisposed) disposed.dispose()
        if(::listDisposable.isInitialized)listDisposable.dispose()
    }
}