package com.ray.personnel.ui.mainpage.favorite

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ray.personnel.Constants.KEY_TOKEN
import com.ray.personnel.data.Company
import com.ray.personnel.domain.database.CompanyDatabaseMethods
import io.reactivex.functions.Consumer

class FavoriteListViewModel(state: SavedStateHandle) : ViewModel() {
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
                    CompanyDatabaseMethods.getLikedByDistanceAsc(onSuccess)
                } else {
                    CompanyDatabaseMethods.getLikedByDistanceDesc(onSuccess)
                }
            }
            SALARY -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getLikedBySalaryAsc(onSuccess)
                } else {
                    CompanyDatabaseMethods.getLikedBySalaryDesc(onSuccess)
                }
            }
            PERCENT -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getLikedByPercentAsc(onSuccess)
                } else {
                    CompanyDatabaseMethods.getLikedByPercentDesc(onSuccess)
                }
            }
        }
    }

    fun getAllByDistanceAsc() {
        CompanyDatabaseMethods.getLikedByDistanceAsc(onSuccess)
    }

    companion object {
        private const val DISTANCE = 0
        private const val SALARY = 1
        private const val PERCENT = 2
    }
}