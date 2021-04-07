package com.ray.personnel.viewmodel.company.list

import android.app.Application
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ray.personnel.viewmodel.FragmentChangeModelInterface


class CompanyListViewModel(application: Application): AndroidViewModel(application), FragmentChangeModelInterface {
    val text = ObservableField<String>()
    override var curFragment = MutableLiveData<Fragment>()



}

/*
MutableLiveData<
String>()
 */