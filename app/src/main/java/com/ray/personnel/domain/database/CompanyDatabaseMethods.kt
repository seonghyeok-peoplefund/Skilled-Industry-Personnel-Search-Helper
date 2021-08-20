package com.ray.personnel.domain.database

import com.ray.personnel.data.Company
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

object CompanyDatabaseMethods {
    fun getAll(onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getAll()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getAllByDistanceAsc(sortType: Int, onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getAllByDistanceAsc(sortType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getAllByDistanceDesc(sortType: Int, onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getAllByDistanceDesc(sortType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getAllBySalaryAsc(sortType: Int, onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getAllBySalaryAsc(sortType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getAllBySalaryDesc(sortType: Int, onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getAllBySalaryDesc(sortType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getAllByPercentAsc(sortType: Int, onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getAllByPercentAsc(sortType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getAllByPercentDesc(sortType: Int, onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getAllByPercentDesc(sortType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getLikedByDistanceAsc(onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getLikedByDistanceAsc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getLikedByDistanceDesc(onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getLikedByDistanceDesc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getLikedBySalaryAsc(onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getLikedBySalaryAsc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getLikedBySalaryDesc(onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getLikedBySalaryDesc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getLikedByPercentAsc(onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getLikedByPercentAsc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getLikedByPercentDesc(onSuccess: (Consumer<in List<Company>>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getLikedByPercentDesc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun getSizeBySortType(sortType: Int, onSuccess: (Consumer<in Int>)): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .getSize(sortType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess)
    }

    fun update(company: Company, onComplete: Action): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .update(company)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete)
    }

    fun updateAll(companies: List<Company>, onComplete: Action): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .updateAll(companies)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete)
    }

    fun insert(company: Company, onComplete: Action): Disposable {
        return CompanyDatabase.getInstance(getApplication())
            .companyDao()
            .insert(company)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete)
    }
}