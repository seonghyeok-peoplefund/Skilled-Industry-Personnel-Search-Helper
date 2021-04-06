package com.ray.personnel.viewmodel.company.filter

import android.app.Application
import android.content.Context
import android.view.View
import androidx.annotation.UiThread
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.kaopiz.kprogresshud.KProgressHUD
import com.ray.personnel.Global
import com.ray.personnel.SupportActivity
import com.ray.personnel.company.Company
import com.ray.personnel.company.Location
import com.ray.personnel.fragment.company.CompanyListFragment
import com.ray.personnel.model.database.CompanyDatabase
import com.ray.personnel.model.parser.CompanyListParser
import com.ray.personnel.viewmodel.FragmentChangeModelInterface
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Jsoup

class CompanyFilterViewModel(application: Application): AndroidViewModel(application), FragmentChangeModelInterface {
    override var curFragment = MutableLiveData<Fragment>()
    lateinit var progress: KProgressHUD



    //@BindingAdapter("onClick")
    fun doFilter(v: View){
        CompanyDatabase.getInstance(getApplication()).companyDao().getSize().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{ size ->
            //왜 여러번될까
            if(size == 0) getCompanyList()
            else curFragment.value = CompanyListFragment()
        }
    }


    var company_stack = 0
    lateinit var listDisposable: Disposable
    fun getCompanyList(){
        CompanyListParser.let{ parse ->
            var i = 0
            listDisposable = Observable.fromPublisher(parse).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { progress = progress_init(getApplication(), 100) }
                    .subscribe(
                            { p ->
                                getCompanyDetail(p)
                                company_stack ++
                                progress_setPercent(progress, ++i) },
                            { err -> progress_fail(progress); println("onError - $err") },
                            { progress_setPercent(progress, ++i) })
        }
    }//"\.(?=(((?!\]).)*\[)|[^\[\]]*$)"
    //\.(?=(((?!\]).)*\[)|[^\[\]]*$) <- Pattern.MULTILINE (?m)
    fun getCompanyDetail(c: Company){
        Observable.fromCallable {
            val doc = JSONObject(Jsoup.connect(Company.WANTED_INFORMATION_URL + c.wanted_id).ignoreContentType(true).execute().body())
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
            c.distance = Location.getDistance(c.location!!.geo_location, Global.curLocation)
            // regex로 괄호 바깥 & 따옴표 바깥에 있는 . 검색, 그뒤에는 제거함. 이후
            //TODO : 알고리즘 바꿔야함.
            c.intro = c.intro.replaceAfter(".", "").replaceBeforeLast("\n", "").replace(Regex(".+\\?"), "").replace(Regex("【[^】]*】"), "").replace(Regex("\\[[^\\]]*\\]"), "").trim()
            c
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{ company ->
            CompanyDatabase.getInstance(getApplication()).companyDao().insert(company)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        company_stack--
                        if(listDisposable.isDisposed && company_stack == 0){
                            progress_success(progress)
                            curFragment.value = CompanyListFragment()
                        }
                    }
        }
        // listDisposable이 마지막 onNext이후 onComplete를 너무 늦게 내버린다면 stack == 0이면서 disposed = false일 수도 있음.
    }

    @UiThread
    fun progress_init(ctx: Context, maxProgress: Int): KProgressHUD {
        val progress = KProgressHUD.create(ctx)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("Please wait")
                .setAutoDismiss(false)
                .setDimAmount(0.5f)
                .setMaxProgress(maxProgress)
                .show()
        return progress
    }
    @UiThread
    fun progress_success(progress: KProgressHUD){
        progress.dismiss()
    }
    @UiThread
    fun progress_fail(progress: KProgressHUD){
        progress.dismiss()
    }
    @UiThread
    fun progress_setPercent(progress: KProgressHUD, percent: Int){
        progress.setProgress(percent)
    }

    override fun onCleared(){
        if(::progress.isInitialized && progress.isShowing) progress.dismiss()
    }
}