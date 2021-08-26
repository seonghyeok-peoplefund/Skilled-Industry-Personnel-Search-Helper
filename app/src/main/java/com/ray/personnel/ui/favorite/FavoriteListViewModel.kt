package com.ray.personnel.ui.favorite

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
import com.ray.personnel.ui.companyinfo.CompanyInfoActivity
import com.ray.personnel.ui.filter.list.CompanyListViewModel
import io.reactivex.functions.Consumer

class FavoriteListViewModel(state: SavedStateHandle) : ViewModel() {
    val loginToken = state.getLiveData<String>(KEY_TOKEN)
    val companies = MutableLiveData<List<Company>>()
    val isNothing = MutableLiveData<Int>(View.INVISIBLE)
    val moveToNextActivity = MutableLiveData<Company>() // 이거 Company 넘기는건 좋은 선택이 아닌것같음. 변수명도 맞지 않음.
    val requireDatabaseMethod = MutableLiveData<>()
    // 작업을 Activity/Fragment에서 해야 하는데, 어떤 작업을 할 지에 관한 정보를 어떻게 넘길 지 생각중

    private val onSuccess: (Consumer<in List<Company>>) = Consumer { arr ->
        companies.value = arr
        if (arr.isEmpty()) {
            isNothing.value = View.VISIBLE
        } else {
            isNothing.value = View.INVISIBLE
        }
    }
    private val onError: Consumer<in Throwable> = Consumer { err ->
        Log.e(TAG, "DB작업중 에러", err)
    }

    val onLikeListener = fun(company: Company, isLiked: Boolean) {
        company.isLiked = isLiked
        CompanyDatabaseMethods.update(
            context,
            company,
            {},
            onError
        )
    }
    val onItemClickListener = fun(company: Company) {
        moveToNextActivity.value = company
    }
    val sortListener = fun(index: Int, isAscendant: Boolean) {
        when (index) {
            DISTANCE -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getLikedByDistanceAsc(
                        context,
                        onSuccess,
                        onError
                    )
                } else {
                    CompanyDatabaseMethods.getLikedByDistanceDesc(
                        context,
                        onSuccess,
                        onError
                    )
                }
            }
            SALARY -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getLikedBySalaryAsc(
                        context,
                        onSuccess,
                        onError
                    )
                } else {
                    CompanyDatabaseMethods.getLikedBySalaryDesc(
                        context,
                        onSuccess,
                        onError
                    )
                }
            }
            PERCENT -> {
                if (isAscendant) {
                    CompanyDatabaseMethods.getLikedByPercentAsc(
                        context,
                        onSuccess,
                        onError
                    )
                } else {
                    CompanyDatabaseMethods.getLikedByPercentDesc(
                        context,
                        onSuccess,
                        onError
                    )
                }
            }
        }
    }

    fun getAllByDistanceAsc() {
        CompanyDatabaseMethods.getLikedByDistanceAsc(
            context,
            onSuccess,
            onError
        )
    }

    companion object {
        private const val TAG = "FavoriteListViewModel"
        private const val DISTANCE = 0
        private const val SALARY = 1
        private const val PERCENT = 2
    }
}