package com.ray.personnel.ui.mainpage.filter.list

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ray.personnel.Constants.KEY_TOKEN
import com.ray.personnel.data.Company
import com.ray.personnel.domain.database.CompanyDatabaseMethods
import com.ray.personnel.domain.parser.CompanyListParser
import io.reactivex.functions.Consumer

class CompanyListViewModel(state: SavedStateHandle) : ViewModel() {
    val loginToken = state.getLiveData<String>(KEY_TOKEN)
    val companies = MutableLiveData<List<Company>>()
    val isNothing = MutableLiveData<Int>(View.INVISIBLE)
    private val onSuccess: (Consumer<in List<Company>>) = Consumer { arr ->
        companies.value = arr
        if (arr.isEmpty()) isNothing.value = View.VISIBLE
        else isNothing.value = View.INVISIBLE
    }

    val sortListener = fun(index: Int, isAscendant: Boolean) {
        when (index) {
            DISTANCE -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getAllByDistanceAsc(CompanyListParser.sortType, onSuccess)
                } else {
                    CompanyDatabaseMethods.getAllByDistanceDesc(CompanyListParser.sortType, onSuccess)
                }
            }
            SALARY -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getAllBySalaryAsc(CompanyListParser.sortType, onSuccess)
                } else {
                    CompanyDatabaseMethods.getAllBySalaryDesc(CompanyListParser.sortType, onSuccess)
                }
            }
            PERCENT -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getAllByPercentAsc(CompanyListParser.sortType, onSuccess)
                } else {
                    CompanyDatabaseMethods.getAllByPercentDesc(CompanyListParser.sortType, onSuccess)
                }
            }
        }
    }

    fun getAllByDistanceAsc() {
        CompanyDatabaseMethods.getAllByDistanceAsc(CompanyListParser.sortType, onSuccess)
    }

    companion object {
        private const val DISTANCE = 0
        private const val SALARY = 1
        private const val PERCENT = 2
    }
}