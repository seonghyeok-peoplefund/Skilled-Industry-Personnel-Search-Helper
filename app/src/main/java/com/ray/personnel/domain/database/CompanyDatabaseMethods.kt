package com.ray.personnel.domain.database

import android.content.Context
import com.ray.personnel.data.Company
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

// 인터넷에서 가져온 내용
object CompanyDatabaseMethods {
    //viewmodel이 fragment/activity 에게 function 객체를 전달하는 것 보다, option을 전달하는 것이 좋다고 생각.
    //그러나 option 전달을 이렇게 하는 것이 좋은지에 대한 확신이 없음.
    const val ASCENDANT = 0x0 shl 0

    const val DESCENDANT = 0x1 shl 0

    const val DISTANCE = 0x0 shl 1

    const val SALARY = 0x1 shl 1

    const val PERCENT = 0x2 shl 1

    const val ALL = 0x0 shl 2

    const val LIKED = 0x1 shl 2

    fun update(
        context: Context,
        company: Company,
        onComplete: Action,
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
        onComplete: Action,
        onError: (Consumer<in Throwable>)
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
        company: Company,
        onComplete: Action,
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return CompanyDatabase.getInstance(context.applicationContext)
            .companyDao()
            .insert(company)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onComplete, onError)
    }

    fun getDataByUsingOptions(
        context: Context,
        options: Int,
        departmentType: Int = -1,
        onSuccess: (Consumer<in List<Company>>),
        onError: (Consumer<in Throwable>)
    ): Disposable {
        return if (options and ALL == ALL) {
            if (options and DISTANCE == DISTANCE) {
                if (options and ASCENDANT == ASCENDANT) {
                    getAllByDistanceAsc(context, departmentType, onSuccess, onError)
                } else {
                    getAllByDistanceDesc(context, departmentType, onSuccess, onError)
                }
            } else if (options and SALARY == SALARY) {
                if (options and ASCENDANT == ASCENDANT) {
                    getAllBySalaryAsc(context, departmentType, onSuccess, onError)
                } else {
                    getAllBySalaryDesc(context, departmentType, onSuccess, onError)
                }
            } else {
                if (options and ASCENDANT == ASCENDANT) {
                    getAllByPercentAsc(context, departmentType, onSuccess, onError)
                } else {
                    getAllByPercentDesc(context, departmentType, onSuccess, onError)
                }
            }
        } else {
            if (options and DISTANCE == DISTANCE) {
                if (options and ASCENDANT == ASCENDANT) {
                    getLikedByDistanceAsc(context, onSuccess, onError)
                } else {
                    getLikedByDistanceDesc(context, onSuccess, onError)
                }
            } else if (options and SALARY == SALARY) {
                if (options and ASCENDANT == ASCENDANT) {
                    getLikedBySalaryAsc(context, onSuccess, onError)
                } else {
                    getLikedBySalaryDesc(context, onSuccess, onError)
                }
            } else {
                if (options and ASCENDANT == ASCENDANT) {
                    getLikedByPercentAsc(context, onSuccess, onError)
                } else {
                    getLikedByPercentDesc(context, onSuccess, onError)
                }
            }
        }
    }

    private fun getAllByDistanceAsc(
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

    private fun getAllByDistanceDesc(
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

    private fun getAllBySalaryAsc(
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

    private fun getAllBySalaryDesc(
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

    private fun getAllByPercentAsc(
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

    private fun getAllByPercentDesc(
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

    private fun getLikedByDistanceAsc(
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

    private fun getLikedByDistanceDesc(
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

    private fun getLikedBySalaryAsc(
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

    private fun getLikedBySalaryDesc(
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

    private fun getLikedByPercentAsc(
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

    private fun getLikedByPercentDesc(
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
}