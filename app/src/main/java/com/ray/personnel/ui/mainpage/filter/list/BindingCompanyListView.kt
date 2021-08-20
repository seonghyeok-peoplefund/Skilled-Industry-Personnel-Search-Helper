package com.ray.personnel.ui.mainpage.filter.list

import android.content.Intent
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ray.personnel.data.Company
import com.ray.personnel.ui.companyinfo.CompanyInfoActivity

@BindingAdapter("onChanged")
fun onCompanyChanged(v: RecyclerView, arr: List<Company>?) {
    if (arr == null) return
    val ctx = v.context
    with(v.adapter as CompanyListAdapter) {
        companies = arr
        notifyDataSetChanged()
        onItemClickListener = fun(view: View, company: Company) {
            val i = Intent(ctx, CompanyInfoActivity::class.java)
            i.putExtra("Company", company)
            ctx.startActivity(i)
        }
    }
}