package com.ray.personnel.viewmodel.company.list

import android.app.Application
import android.content.Intent
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ray.personnel.Global
import com.ray.personnel.R
import com.ray.personnel.company.Company
import com.ray.personnel.fragment.company.CompanyInfo
import com.ray.personnel.model.database.CompanyDatabase
import com.ray.personnel.viewmodel.FragmentChangeModelInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class CompanyListViewModel(application: Application): AndroidViewModel(application), FragmentChangeModelInterface {
    val text = ObservableField<String>()
    override var curFragment = MutableLiveData<Fragment>()



}

/*
MutableLiveData<
String>()
 */