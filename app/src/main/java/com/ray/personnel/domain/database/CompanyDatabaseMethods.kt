package com.ray.personnel.domain.database

import android.content.Context
import com.ray.personnel.data.Company
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

object CompanyDatabaseMethods {
    fun getAll(
        context: Context,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getAll()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getAllByDistanceAsc(
        context: Context,
        departmentType: Int,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getAllByDistanceAsc(departmentType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getAllByDistanceDesc(
        context: Context,
        departmentType: Int,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getAllByDistanceDesc(departmentType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getAllBySalaryAsc(
        context: Context,
        departmentType: Int,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getAllBySalaryAsc(departmentType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getAllBySalaryDesc(
        context: Context,
        departmentType: Int,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getAllBySalaryDesc(departmentType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getAllByPercentAsc(
        context: Context,
        departmentType: Int,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getAllByPercentAsc(departmentType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getAllByPercentDesc(
        context: Context,
        departmentType: Int,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getAllByPercentDesc(departmentType)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getLikedByDistanceAsc(
        context: Context,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getLikedByDistanceAsc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getLikedByDistanceDesc(
        context: Context,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getLikedByDistanceDesc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getLikedBySalaryAsc(
        context: Context,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getLikedBySalaryAsc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getLikedBySalaryDesc(
        context: Context, 
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getLikedBySalaryDesc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getLikedByPercentAsc(
        context: Context, 
        onSuccess: (Consumer<in List<Company>>), 
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getLikedByPercentAsc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getLikedByPercentDesc(
        context: Context, 
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getLikedByPercentDesc()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun getSizeByDepartmentType(
        context: Context,
        departmentType: Int,
        onSuccess: (Consumer<in Int>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .getSize(departmentType)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
    }

    fun update(
        context: Context, 
        company: Company, onComplete: Action,
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .update(company)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, onError)
    }

    fun update(
        context: Context, 
        companies: List<Company>, 
        onComplete: Action, onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .update(companies)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, onError)
    }

    fun insert(
        context: Context, 
        company: Company, onComplete: Action,
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .insert(company)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, onError)
    }
}