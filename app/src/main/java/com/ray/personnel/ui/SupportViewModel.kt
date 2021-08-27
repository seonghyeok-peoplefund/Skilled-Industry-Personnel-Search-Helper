package com.ray.personnel.ui

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ray.personnel.R

class SupportViewModel : ViewModel() {
    val selectedNavItemId = MutableLiveData(R.id.icon_company)

    fun navItemSelected(item: MenuItem): Boolean {
        selectedNavItemId.value = item.itemId
        return true
    }
}