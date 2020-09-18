package com.andysklyarov.finnotifyfree.ui

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.andysklyarov.domain.model.CurrencyInRub
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.R
import com.andysklyarov.finnotifyfree.alarm.AlarmServiceManager
import com.andysklyarov.finnotifyfree.alarm.ServiceState
import com.andysklyarov.domain.usecases.CurrencyUsecase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.math.abs


class MainViewModel @Inject constructor(application: AppDelegate) : AndroidViewModel(application) {
    val currency = ObservableField<CurrencyInRub>()
    val diffCurrency = ObservableField<String>()

    val topLimit = ObservableField<Float>()
    val bottomLimit = ObservableField<Float>()

    val isServiceStarted = ObservableField<Boolean>()
    val isLoading = ObservableField<Boolean>()
    val errorVisibility = ObservableField<Int>()
    val mainPartsVisibility = ObservableField<Int>()
    val backgroundRes = ObservableField<Int>()

    private var disposable: Disposable? = null

    private var alarmManager: AlarmServiceManager

    @Inject
    lateinit var usecase: CurrencyUsecase


    init {
        currency.set(
            CurrencyInRub(
                getApplication<AppDelegate>().getString(R.string.default_currency_name),
                getApplication<AppDelegate>().getString(R.string.default_currency_code),
                LocalDate.of(0, 1, 1),
                0,
                0.0f,
                0.0f
            )
        )
        diffCurrency.set("0")
        alarmManager = AlarmServiceManager(getApplication())
        updateAlarmState()
        isLoading.set(false)
        mainPartsVisibility.set(View.GONE)
        errorVisibility.set(View.GONE)
        val imgResId = application.loadImgRes()
        backgroundRes.set(imgResId)
    }

    fun updateData(currencyCode: String) {
        disposable = usecase.getLastCurrency(currencyCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoading.set(true) }
            .doFinally { isLoading.set(false) }
            .subscribe({
                currency.set(it)

                if (it.diff > 0) {
                    diffCurrency.set("+" + it.diff)
                } else {
                    diffCurrency.set(it.diff.toString())
                }

                setBackground(it.diff)
                mainPartsVisibility.set(View.VISIBLE)
                errorVisibility.set(View.GONE)

            }) {
                mainPartsVisibility.set(View.GONE)
                errorVisibility.set(View.VISIBLE)
            }
    }

    fun dispatchDetach() {
        disposable?.dispose()
    }

    fun enableAlarm(hour: Int, minutes: Int, topLimit: Float, bottomLimit: Float) {
        this.topLimit.set(topLimit)
        this.bottomLimit.set(bottomLimit)
        alarmManager.setAlarmTime24(hour, minutes)
        alarmManager.startRepeatingService(currency.get()!!.chCode, topLimit, bottomLimit)
        updateAlarmState()
    }

    fun disableAlarm() {
        alarmManager.stopRepeatingService()
        updateAlarmState()
    }

    fun getAlarmTime(): ZonedDateTime {
        return alarmManager.getAlarmTime()
    }

    fun getDatePreamble(): String {
        return getApplication<AppDelegate>().getString(R.string.update_on)
    }

    fun getNomPreamble(nom: Int, currencyName: String): String {
        return getApplication<AppDelegate>().getString(R.string.nom_string) + " " + nom + " " + currencyName
    }


    private fun updateAlarmState() {
        val state: ServiceState = alarmManager.getServiceState()
        topLimit.set(state.topLimit)
        bottomLimit.set(state.bottomLimit)
        isServiceStarted.set(state.isStarted)
    }

    private fun setBackground(diff: Float) {
        val absoluteDiff = abs(diff)
        if (absoluteDiff < 0.2) { // todo add to settings
            backgroundRes.set(R.mipmap.img1)
        } else if (absoluteDiff > 0.2 && absoluteDiff < 0.4) {
            backgroundRes.set(R.mipmap.img2)
        } else {
            backgroundRes.set(R.mipmap.img3)
        }
    }

}