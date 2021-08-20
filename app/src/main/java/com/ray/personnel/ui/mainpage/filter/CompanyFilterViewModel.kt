package com.ray.personnel.ui.mainpage.filter

import android.Manifest
import android.util.Log
import android.widget.AdapterView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ray.personnel.Constants.KEY_TOKEN
import com.ray.personnel.data.Company
import com.ray.personnel.data.GeoLocation
import com.ray.personnel.domain.LocationManager
import com.ray.personnel.domain.database.CompanyDatabaseMethods
import com.ray.personnel.domain.parser.CompanyDetailParser
import com.ray.personnel.domain.parser.CompanyListParser
import de.timonknispel.ktloadingbutton.KTLoadingButton
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class CompanyFilterViewModel(state: SavedStateHandle) : ViewModel() {
    val loginToken = state.getLiveData<String>(KEY_TOKEN)
    val moveToNextFragment = MutableLiveData<Boolean>()
    val permissionRequested = MutableLiveData<List<String>>()
    val currentPermission = MutableLiveData<MutableList<String>>()
    val toastMessage = MutableLiveData<String>()
    val jobs1 = MutableLiveData<List<String>>()
    val jobs2 = MutableLiveData<List<String>>()
    val jobs1value = MutableLiveData<Int>()
    val jobs2value = MutableLiveData<Int>()
    val jobs1listener = MutableLiveData<AdapterView.OnItemSelectedListener>()
    val jobs2listener = MutableLiveData<AdapterView.OnItemSelectedListener>()
    var latitude = MutableLiveData<String>()
    var longitude = MutableLiveData<String>()
    var warningColor = MutableLiveData<Int>()
    var warningText = MutableLiveData<String>()
    val progressMax = MutableLiveData(100)
    val progressCurrent = MutableLiveData(0)
    private val sortType: Int get() = CompanyListParser.sortType
    private val beDisposed = mutableListOf<Disposable>()
    lateinit var listDisposable: Disposable
    private var companyStack = 0

    fun useGps(v: KTLoadingButton?) {
        permissionRequested.value = listOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (currentPermission.value?.containsAll(permissionRequested.value!!) == true) {
            beDisposed.add(
                LocationManager.getLocation(
                    { loc ->
                        latitude.value = loc?.latitude.toString()
                        longitude.value = loc?.longitude.toString()
                        v?.doResult(true)
                    },
                    { err ->
                        v?.doResult(false)
                        Log.e(TAG, "위치를 가져오지 못했습니다.", err)
                    }
                )
            )
        } else {
            v?.reset()
        }
    }

    private fun getCompanyList() {
        if (CompanyListParser.sortType == -1) {
            toastMessage.value = "분야를 선택해주세요."
            return
        }
        if (latitude.value.isNullOrBlank() || longitude.value.isNullOrBlank()) {
            toastMessage.value = "위치를 선택해주세요."
            return
        }
        if (CompanyListParser.isNotParsing()) {
            listDisposable =
                Observable.fromPublisher(CompanyListParser)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe {
                        progressMax.value = 100
                        progressCurrent.value = 0
                        warningColor.value = (0xff shl 24) or 0x000000
                        warningText.value = "Parsing 시작. 20초~60초 가량 기다려주세요."
                    }
                    .subscribe(
                        { p ->
                            getCompanyDetail(p)
                            companyStack++
                            progressCurrent.value = progressCurrent.value?.plus(1)
                        },
                        { err ->
                            progressCurrent.value = 0
                            Log.e(TAG, "파싱 도중 에러 발생.", err)
                        },
                        {
                            warningText.value = "대기중"
                            progressCurrent.value = 0
                            if (CompanyListParser.itemCount == 0) moveToNextFragment.value = true
                        }
                    )
        } else {
            toastMessage.value = "현재 정보를 로딩중입니다. 잠시 기다려주세요."
        }
    }

    fun doFilter() {
        beDisposed.add(
            CompanyDatabaseMethods.getSizeBySortType(sortType) { size ->
                if (size == 0) getCompanyList() else updateDistance()
            }
        )
    }

    private fun updateDistance() {
        beDisposed.add(
            CompanyDatabaseMethods.getAll() { companies ->
                companies.forEach { company ->
                    company.distance = LocationManager.getDistance(
                        company.location!!.geoLocation,
                        GeoLocation(latitude.value!!.toDouble(), longitude.value!!.toDouble())
                    )
                }
                beDisposed.add(
                    CompanyDatabaseMethods.updateAll(companies) {
                        moveToNextFragment.value = true
                    }
                )
            }
        )
    }

    private fun getCompanyDetail(company: Company) {
        val currentGeoLocation = GeoLocation(latitude.value!!.toDouble(), longitude.value!!.toDouble())
        CompanyDetailParser.initDetail(company, currentGeoLocation, loginToken.value!!) { company ->
            beDisposed.add(
                CompanyDatabaseMethods.insert(company) {
                    companyStack--
                    if (listDisposable.isDisposed && companyStack == 0) {
                        moveToNextFragment.value = true
                    }
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        for (disposed in beDisposed) disposed.dispose()
        if (::listDisposable.isInitialized) listDisposable.dispose()
    }

    companion object {
        private const val TAG = "CompanyFilterViewModel"
    }
}