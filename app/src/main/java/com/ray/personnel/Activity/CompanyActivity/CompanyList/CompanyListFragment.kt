package com.ray.personnel.Activity.CompanyActivity.CompanyList

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.ray.personnel.Activity.CompanyActivity.CompanyInfo.CompanyInfo
import com.ray.personnel.Activity.Global
import com.ray.personnel.Activity.Global.gson
import com.ray.personnel.Activity.SupportActivity
import com.ray.personnel.Company.Company
import com.ray.personnel.Company.CompanyDatabase
import com.ray.personnel.Company.Location
import com.ray.personnel.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class CompanyListFragment() : Fragment() {
    lateinit var ctx: Context

    lateinit var companies: List<Company>
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.company_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView(view)
        setSpinner(view)
        getCompany(view)
    }

    private fun getCompany(view: View){
        CompanyDatabase.getInstance(ctx).companyDao().getAllByDistance()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies = arr
                    view.findViewById<RecyclerView>(R.id.list).adapter = CompanyListAdapter(ctx, arr)
                    (view.findViewById<RecyclerView>(R.id.list).adapter as CompanyListAdapter).setOnItemClickListener { view: View, company: Company -> run{
                        val i = Intent(ctx, CompanyInfo::class.java)
                        i.putExtra("Company", gson.toJson(company))
                        startActivity(i)
                        activity?.overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out)
                    }}
                }
    }
    private fun setSpinner(view: View) {
        view.findViewById<Spinner>(R.id.company_list_sp1).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("중소기업", "중견기업", "대기업"))
        view.findViewById<Spinner>(R.id.company_list_sp2).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("1km", "5km", "10km", "50km", "100km", "그 외"))
        view.findViewById<Spinner>(R.id.company_list_sp3).adapter = ArrayAdapter(ctx, android.R.layout.simple_list_item_1, Arrays.asList("5점", "4점", "3점", "2점", "1점"))

        view.findViewById<Spinner>(R.id.company_list_sp2).onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, v: View, position: Int, id: Long) {

            }
        }
    }

    private fun setRecyclerView(view: View) {
        with(view.findViewById<RecyclerView>(R.id.list)){
            layoutManager = GridLayoutManager(ctx, 2)
            addItemDecoration(getGridDecoration())
        }
    }

    private fun getGridDecoration(): ItemDecoration = object : ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val maxCount = parent.adapter!!.itemCount
            val position = parent.getChildAdapterPosition(view)
            val spanCount = 2
            val spacing = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, resources.displayMetrics).toInt()
            val outerMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, resources.displayMetrics).toInt()
            val column = position % spanCount
            val row = position / spanCount
            val lastRow = (maxCount - 1) / spanCount
            outRect.left = column * spacing / spanCount
            outRect.right = spacing - (column + 1) * spacing / spanCount
            outRect.top = spacing * 2
            if (row == lastRow) {
                outRect.bottom = outerMargin
            }
        }
    }

}
