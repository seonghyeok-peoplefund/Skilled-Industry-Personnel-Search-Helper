package com.ray.personnel.viewmodel.company.list

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


class CompanyListViewModel(application: Application): AndroidViewModel(application), FragmentChangeModelInterface {
    override var curFragment = MutableLiveData<Fragment>()
    override val permissionRequest = MutableLiveData<List<String>>()
    override val permissionResult = MutableLiveData<List<String>>()
    val companies = MutableLiveData<List<Company>>()
    val sortText = MutableLiveData<String>("Sorted by distance")

    val sortListener = object: SortRadioGroup.SortRadioListener{
        override fun onClick(index: Int, isAscendant: Boolean) {
            when(index){
                DISTANCE -> {
                    println("sort by 1")
                    if(isAscendant) getAllByDistanceAsc()
                    else getAllByDistanceDesc()
                    sortText.value = "Sorted by distance"
                }
                SALARY -> {
                    println("sort by 2")
                    if(isAscendant) getAllBySalaryAsc()
                    else getAllBySalaryDesc()
                    sortText.value = "Sorted by salary"

                }
                LIKE -> {
                    println("sort by 3")
                    if(isAscendant) getAllByLikeAsc()
                    else getAllByLikeDesc()
                    sortText.value = "Sorted by liked"

                }
            }
        }
    }

    fun getAllByDistanceAsc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllByDistanceAsc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr -> companies.value = arr }
    }

    private fun getAllByDistanceDesc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllByDistanceDesc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr -> companies.value = arr }
    }

    private fun getAllBySalaryAsc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllBySalaryAsc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr -> companies.value = arr }
    }

    private fun getAllBySalaryDesc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllBySalaryDesc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr -> companies.value = arr }
    }

    private fun getAllByLikeAsc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllByLikeAsc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr -> companies.value = arr }
    }

    private fun getAllByLikeDesc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllByLikeDesc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr -> companies.value = arr }
    }

    companion object{
        private const val DISTANCE = 0
        private const val SALARY = 1
        private const val LIKE = 2
    }
}

/*
MutableLiveData<
String>()
 */