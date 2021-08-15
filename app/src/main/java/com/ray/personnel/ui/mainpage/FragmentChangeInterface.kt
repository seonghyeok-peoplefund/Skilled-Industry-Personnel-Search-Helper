package com.ray.personnel.ui.mainpage

import androidx.lifecycle.MutableLiveData

interface FragmentChangeInterface {
    var isAttached: MutableLiveData<Any?>
    val model: FragmentChangeModelInterface
}