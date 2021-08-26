package com.ray.personnel.ui.filter.list

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.like.LikeButton
import com.like.OnLikeListener
import com.ray.personnel.Constants.KEY_TOKEN
import com.ray.personnel.data.Company
import com.ray.personnel.domain.database.CompanyDatabaseMethods
import com.ray.personnel.domain.parser.CompanyListParser
import com.ray.personnel.ui.companyinfo.CompanyInfoActivity
import com.ray.personnel.ui.favorite.FavoriteListViewModel
import io.reactivex.functions.Consumer

//TODO("Favorite 작업 완료하고, 합치거나 코드를 가져오거나 할거임. 그 때까지 보류")
class CompanyListViewModel(state: SavedStateHandle) : ViewModel() {
    val loginToken = state.getLiveData<String>(KEY_TOKEN)
    val companies = MutableLiveData<List<Company>>()
    val isNothing = MutableLiveData<Int>(View.INVISIBLE)
    val requireDatabaseMethod = MutableLiveData<>()

    private val onSuccess: (Consumer<in List<Company>>) = Consumer { arr ->
        companies.value = arr
        if (arr.isEmpty()) isNothing.value = View.VISIBLE
        else isNothing.value = View.INVISIBLE
    }
    private val onError: Consumer<in Throwable> = Consumer { err ->
        Log.e(TAG, "DB작업중 에러", err)
    }

    val onLikeListener = fun(company: Company, isLiked: Boolean) {
        company.isLiked = isLiked
        CompanyDatabaseMethods.update(company) {}
    }
    val onItemClickListener = fun(company: Company) {
        moveToNextActivity.value = company
    }
    val sortListener = fun(index: Int, isAscendant: Boolean) {
        when (index) {
            DISTANCE -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getAllByDistanceAsc(CompanyListParser.departmentType, onSuccess, onError)
                } else {
                    CompanyDatabaseMethods.getAllByDistanceDesc(CompanyListParser.departmentType, onSuccess, onError)
                }
            }
            SALARY -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getAllBySalaryAsc(CompanyListParser.departmentType, onSuccess, onError)
                } else {
                    CompanyDatabaseMethods.getAllBySalaryDesc(CompanyListParser.departmentType, onSuccess, onError)
                }
            }
            PERCENT -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getAllByPercentAsc(CompanyListParser.departmentType, onSuccess)
                } else {
                    CompanyDatabaseMethods.getAllByPercentDesc(CompanyListParser.departmentType, onSuccess)
                }
            }
        }
    }

    fun getAllByDistanceAsc() {
        CompanyDatabaseMethods.getAllByDistanceAsc(CompanyListParser.departmentType, onSuccess)
    }

    companion object {
        private const val TAG = "CompanyListViewModel"

        private const val DISTANCE = 0
        private const val SALARY = 1
        private const val PERCENT = 2
    }
}