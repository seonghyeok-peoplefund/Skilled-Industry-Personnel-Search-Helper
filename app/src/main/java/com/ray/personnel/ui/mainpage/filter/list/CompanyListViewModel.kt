package com.ray.personnel.ui.mainpage.filter.list

import android.app.Application
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.ray.personnel.data.Company
import com.ray.personnel.widget.SortRadioGroup
import com.ray.personnel.domain.database.CompanyDatabase
import com.ray.personnel.domain.parser.CompanyListParser
import com.ray.personnel.ui.mainpage.FragmentChangeModelInterface
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CompanyListViewModel(application: Application): AndroidViewModel(application),
    FragmentChangeModelInterface {
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
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllByDistanceAsc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllByDistanceDesc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllByDistanceDesc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllBySalaryAsc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllBySalaryAsc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllBySalaryDesc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllBySalaryDesc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllByPercentAsc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllByPercentAsc(CompanyListParser.sortType)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{ arr ->
                    companies.value = arr
                    if(arr.isEmpty()) isNothing.value = View.VISIBLE
                    else isNothing.value = View.INVISIBLE
                }
    }

    private fun getAllByPercentDesc(){
        CompanyDatabase.getInstance(getApplication()).companyDao().getAllByPercentDesc(CompanyListParser.sortType)
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