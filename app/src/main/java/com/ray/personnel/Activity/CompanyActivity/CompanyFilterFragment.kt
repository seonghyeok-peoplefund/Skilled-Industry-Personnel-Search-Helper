package com.ray.personnel.Activity.CompanyActivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kaopiz.kprogresshud.KProgressHUD
import com.ray.personnel.Activity.CompanyActivity.CompanyList.CompanyListFragment
import com.ray.personnel.Activity.SupportActivity
import com.ray.personnel.Company.CompanyDatabase
import com.ray.personnel.Parser.WantedParser
import com.ray.personnel.Parser.WantedParser.Companion.CHECKING_MILITARY
import com.ray.personnel.Parser.WantedParser.Companion.MAX_PRESUMED_COUNT
import com.ray.personnel.Parser.WantedParser.Companion.MAX_SEARCH_COUNT
import com.ray.personnel.Parser.WantedParser.Companion.PARSING_WANTED
import com.ray.personnel.Parser.WantedParser.Companion.SEARCH_FINISHED
import com.ray.personnel.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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
        view.findViewById<Spinner>(R.id.sp1).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("개발", "경영/비즈니스", "마케팅/광고", "디자인", "영ㅇㅂ", "고객서비스/리테일", "미디어", "인사", "게임 제작", "금융", "물류/무역", "엔지니어링/설계", "의료/제약/바이오", "제조/생산", "교육", "식/음료", "법률/법집행기관", "건설/시설", "공공/복지"))
        view.findViewById<Spinner>(R.id.sp2).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("<비어 있음>"))
        view.findViewById<Spinner>(R.id.sp3).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("원티드"))
        view.findViewById<Spinner>(R.id.sp4).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("서울"))
        view.findViewById<FloatingActionButton>(R.id.btn1).setOnClickListener {
            CompanyDatabase.getInstance(ctx).companyDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{companies ->
                    if(companies.isNotEmpty()) {
                        (activity as SupportActivity).loadFragment(CompanyListFragment(companies), true)
                    } else{
                        WantedParser().let{ parse ->
                            var describedInt: Int = (MAX_PRESUMED_COUNT / MAX_SEARCH_COUNT) * 2
                            var i = 0
                            Observable.fromPublisher(parse)
                                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe {
                                    progress = progress_init(ctx, (SEARCH_FINISHED - CHECKING_MILITARY + PARSING_WANTED + describedInt) )
                                }
                                .subscribe(
                                    { p ->
                                        progress_setPercent(progress, ++i) },
                                    { err -> progress_fail(progress); println("onError - $err") },
                                    {
                                        progress_setPercent(progress, ++i)
                                        CompanyDatabase.getInstance(ctx).companyDao().insertAll(parse.output)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe{
                                                progress_success(progress)
                                                (activity as SupportActivity).loadFragment(CompanyListFragment(parse.output), true)
                                            }

                                    }
                                )
                        }
                    }
                }
        }

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