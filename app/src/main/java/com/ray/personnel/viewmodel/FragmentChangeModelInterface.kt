package com.ray.personnel.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

interface FragmentChangeModelInterface {
    var curFragment: MutableLiveData<Fragment>
    val permissionRequest: MutableLiveData<List<String>>
    val permissionResult: MutableLiveData<List<String>>
}