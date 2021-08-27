package com.ray.personnel.ui.favorite

import android.util.Log
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

    val isNothing = MutableLiveData(View.INVISIBLE)

    val moveToNextActivity = MutableLiveData<Company>()

    val requestDatabaseGetMethod = MutableLiveData<Int>()

    val requestDatabaseUpdateMethod = MutableLiveData<Company>()

    val onSuccess: (Consumer<in List<Company>>) = Consumer { arr ->
        companies.value = arr
        if (arr.isEmpty()) {
            isNothing.value = View.VISIBLE
        } else {
            isNothing.value = View.INVISIBLE
        }
    }

    val onError: Consumer<in Throwable> = Consumer { err ->
        Log.e(TAG, "DB작업중 에러", err)
    }

    val onLikeListener = fun(company: Company, isLiked: Boolean) {
        company.isLiked = isLiked
        requestDatabaseUpdateMethod.value = company
    }

    val onItemClickListener = fun(company: Company) {
        moveToNextActivity.value = company
    }

    val sortListener = fun(index: Int, isAscendant: Boolean) {
        var value = CompanyDatabaseMethods.LIKED
        when (index) {
            DISTANCE -> {
                value = value or
                        CompanyDatabaseMethods.DISTANCE or
                        if (isAscendant) {
                            CompanyDatabaseMethods.ASCENDANT
                        } else {
                            CompanyDatabaseMethods.DESCENDANT
                        }
            }
            SALARY -> {
                value = value or CompanyDatabaseMethods.SALARY or
                        if (isAscendant) {
                            CompanyDatabaseMethods.ASCENDANT
                        } else {
                            CompanyDatabaseMethods.DESCENDANT
                        }
            }
            PERCENT -> {
                value = value or CompanyDatabaseMethods.PERCENT or
                        if (isAscendant) {
                            CompanyDatabaseMethods.ASCENDANT
                        } else {
                            CompanyDatabaseMethods.DESCENDANT
                        }
            }
        }
        requestDatabaseGetMethod.value = value
    }

    fun initData() {
        requestDatabaseGetMethod.value = CompanyDatabaseMethods.LIKED or
                CompanyDatabaseMethods.DISTANCE or
                CompanyDatabaseMethods.ASCENDANT
    }

    companion object {
        private const val TAG = "FavoriteListViewModel"
        private const val DISTANCE = 0
        private const val SALARY = 1
        private const val PERCENT = 2
    }
}