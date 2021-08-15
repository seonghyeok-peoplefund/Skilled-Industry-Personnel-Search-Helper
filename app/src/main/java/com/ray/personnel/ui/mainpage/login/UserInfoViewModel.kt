package com.ray.personnel.ui.mainpage.login

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ray.personnel.Constants
import com.ray.personnel.ui.mainpage.FragmentChangeModelInterface

class UserInfoViewModel(application: Application): AndroidViewModel(application),
    FragmentChangeModelInterface {
    override var curFragment = MutableLiveData<Fragment>()
    override val permissionRequest = MutableLiveData<List<String>>()
    override val permissionResult = MutableLiveData<List<String>>()
    val webViewUrl = MutableLiveData<String>(Constants.WANTED_LOGIN)
}