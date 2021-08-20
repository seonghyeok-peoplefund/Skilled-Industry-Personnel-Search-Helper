package com.ray.personnel.ui.mainpage

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ray.personnel.R

class SupportViewModel : ViewModel() {
    val selectedNavItemId = MutableLiveData<Int>(R.id.icon_company)

    fun navItemSelected(item: MenuItem): Boolean {
        selectedNavItemId.value = item.itemId
        return true
    }
}