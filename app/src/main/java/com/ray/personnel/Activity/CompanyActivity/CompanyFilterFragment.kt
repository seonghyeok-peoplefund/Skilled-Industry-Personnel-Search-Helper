package com.ray.personnel.Activity.CompanyActivity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kaopiz.kprogresshud.KProgressHUD
import com.ray.personnel.Activity.CompanyActivity.CompanyList.CompanyListFragment
import com.ray.personnel.Activity.Global
import com.ray.personnel.Activity.SupportActivity
import com.ray.personnel.Company.Company
import com.ray.personnel.Company.CompanyDatabase
import com.ray.personnel.Company.CompanyOccupation
import com.ray.personnel.Company.Location
import com.ray.personnel.Parser.CompanyListParser
import com.ray.personnel.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.*


class CompanyFilterFragment : Fragment() {
    lateinit var progress: KProgressHUD
    lateinit var ctx: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.company_filter, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(view)
        view.findViewById<FloatingActionButton>(R.id.btn1).setOnClickListener {
            CompanyDatabase.getInstance(ctx).companyDao().getSize().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe{size ->
                //왜 여러번될까
                if(size == 0) getCompanyList()
                else (activity as SupportActivity).loadFragment(CompanyListFragment(), true)
            }
        }

    }
    var company_stack = 0
    lateinit var listDisposable: Disposable
    fun getCompanyList(){
        CompanyListParser.let{ parse ->
            var i = 0
            listDisposable = Observable.fromPublisher(parse).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { progress = progress_init(ctx, 100) }
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
            CompanyDatabase.getInstance(ctx).companyDao().insert(company)
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        company_stack--
                        if(listDisposable.isDisposed && company_stack == 0){
                            progress_success(progress)
                            (activity as SupportActivity).loadFragment(CompanyListFragment(), true)
                        }
                    }
        }
        // listDisposable이 마지막 onNext이후 onComplete를 너무 늦게 내버린다면 stack == 0이면서 disposed = false일 수도 있음.
    }
    fun initAdapter(view: View){

        view.findViewById<Spinner>(R.id.sp1).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, CompanyOccupation.occupation.keys.toList())
        view.findViewById<Spinner>(R.id.sp2).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("<비어 있음>"))
        view.findViewById<Spinner>(R.id.sp1).onItemSelectedListener = object: OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                view.findViewById<Spinner>(R.id.sp2).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, (CompanyOccupation.occupation[parent.getItemAtPosition(position).toString()]!!).keys.toList())

            }

        }
        view.findViewById<Spinner>(R.id.sp2).onItemSelectedListener = object: OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                println(CompanyOccupation.occupation[view.findViewById<Spinner>(R.id.sp1).selectedItem.toString()]?.get(parent.getItemAtPosition(position).toString()))
            }

        }/*
        view.findViewById<Spinner>(R.id.sp1).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("<비어 있음>"))
        view.findViewById<Spinner>(R.id.sp2).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("<비어 있음>"))*/
        view.findViewById<Spinner>(R.id.sp3).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("원티드"))
        view.findViewById<Spinner>(R.id.sp4).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("서울"))
    }
    @UiThread
    fun progress_init(ctx: Context, maxProgress: Int): KProgressHUD{
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

    override fun onDestroy() {
        if(::progress.isInitialized && progress.isShowing) progress.dismiss()
        super.onDestroy()
    }
}

