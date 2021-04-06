package com.ray.personnel.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ray.personnel.viewmodel.FragmentChangeModelInterface
import com.ray.personnel.viewmodel.SupportViewModel

interface FragmentChangeInterface {
    var isAttached: MutableLiveData<Any?>
    val model: FragmentChangeModelInterface
}