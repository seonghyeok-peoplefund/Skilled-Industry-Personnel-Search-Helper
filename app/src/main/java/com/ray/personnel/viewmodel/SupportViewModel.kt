package com.ray.personnel.viewmodel

import android.app.Application
import android.view.MenuItem
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ray.personnel.fragment.company.CompanyFilterFragment
import com.ray.personnel.R
import com.ray.personnel.fragment.favorite.FavoriteListFragment
import com.ray.personnel.fragment.user.info.UserInfoFragment

class SupportViewModel(application: Application): AndroidViewModel(application){
    val text = ObservableField<String>()
    val curFragment = MutableLiveData<Fragment>(CompanyFilterFragment())

    /*
    MutableLiveData<
    String>()
     */

    /*
    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        with((menu!!.findItem(R.id.search).actionView) as SearchView){
            setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextChange(newText: String): Boolean {
                    return true
                }

                override fun onQueryTextSubmit(query: String): Boolean {
                    return true
                }
            })
        }
        return true
    }*/
    fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        /*
        R.id.search -> {
            true
        }
        R.id.menu -> {
            //findViewById<DrawerLayout>(R.id.drawer).openDrawer(Gravity.RIGHT)
            true
        }
        android.R.id.home -> {
            //finish()
            curFragment.value = CompanyFilterFragment()
            true
        }*/
        else -> null
    }

    fun navItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
            R.id.icon_company -> curFragment.value = CompanyFilterFragment()
            R.id.icon_account -> curFragment.value = UserInfoFragment()
            R.id.icon_favorite -> curFragment.value = FavoriteListFragment()
        }
        return true
    }
}