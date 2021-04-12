package com.ray.personnel.utils.bindingadapter

import android.content.Intent
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ray.personnel.Global
import com.ray.personnel.R
import com.ray.personnel.company.Company
import com.ray.personnel.fragment.company.CompanyInfo
import com.ray.personnel.ui.SortRadioGroup
import com.ray.personnel.viewmodel.company.list.CompanyListAdapter
import java.util.concurrent.Callable


@BindingAdapter("onChanged")
fun onCompanyChanged(v: RecyclerView, arr: List<Company>?){ // 원소/메소드를 viewmodel에서 넘겨줌.
    if(arr == null) return
    val ctx = v.context
    with(v.adapter as CompanyListAdapter){
        companies = arr
        notifyDataSetChanged()
        setOnItemClickListener { view: View, company: Company -> run{
            val i = Intent(ctx, CompanyInfo::class.java)
            i.putExtra("Company", Global.gson.toJson(company))
            ctx.startActivity(i)
            //v.overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out)
        }}
    }
}