package com.ray.personnel.viewmodel

import androidx.lifecycle.MutableLiveData

interface FragmentChangeInterface {
    val curFragment: MutableLiveData<Int>
}