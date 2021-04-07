package com.ray.personnel.viewmodel.user.info

import android.app.Application
import android.webkit.WebView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ray.personnel.utils.Constants
import com.ray.personnel.viewmodel.FragmentChangeModelInterface

class UserInfoViewModel(application: Application): AndroidViewModel(application), FragmentChangeModelInterface {
    override var curFragment = MutableLiveData<Fragment>()
    val webViewUrl = MutableLiveData<String>(Constants.WANTED_LOGIN)
}