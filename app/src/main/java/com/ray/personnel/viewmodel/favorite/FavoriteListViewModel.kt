package com.ray.personnel.viewmodel.favorite

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ray.personnel.Global
import com.ray.personnel.R
import com.ray.personnel.company.Company
import com.ray.personnel.fragment.company.CompanyInfo
import com.ray.personnel.ui.SortRadioButton
import com.ray.personnel.ui.SortRadioGroup
import com.ray.personnel.utils.database.CompanyDatabase
import com.ray.personnel.utils.parser.CompanyListParser
import com.ray.personnel.viewmodel.FragmentChangeModelInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class FavoriteListViewModel(application: Application): AndroidViewModel(application), FragmentChangeModelInterface {
    override var curFragment = MutableLiveData<Fragment>()
    override val permissionRequest = MutableLiveData<List<String>>()
    override val permissionResult = MutableLiveData<List<String>>()
    val companies = MutableLiveData<List<Company>>()
    val isNothing = MutableLiveData<Int>(View.INVISIBLE)

    val sortListener = object: SortRadioGroup.SortRadioListener{
        override fun onClick(index: Int, isAscendant: Boolean) {
            when(index){
                DISTANCE -> {
                    if(isAscendant) getAllByDistanceAsc()
                    else getAllByDistanceDesc()
                }
                SALARY -> {
                    if(isAscendant) getAllBySalaryAsc()
                    else getAllBySalaryDesc()
                }
                PERCENT -> {
                    if(isAscendant) getAllByPercentAsc()
                    else getAllByPercentDesc()
                }
            }
        }
    }

    fun getAllByDistanceAsc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getLikedByDistanceAsc()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllByDistanceDesc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getLikedByDistanceDesc()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllBySalaryAsc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getLikedBySalaryAsc()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllBySalaryDesc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getLikedBySalaryDesc()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllByPercentAsc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getLikedByPercentAsc()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllByPercentDesc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getLikedByPercentDesc()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }



    companion object{
        private const val DISTANCE = 0
        private const val SALARY = 1
        private const val PERCENT = 2
    }
}

/*
MutableLiveData<
String>()
 */