package com.andysklyarov.finnotify.presentation

import android.app.Application
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import com.andysklyarov.finnotify.R
import com.andysklyarov.finnotify.domain.CurrencyInRub
import com.andysklyarov.finnotify.domain.CurrencyName
import com.andysklyarov.finnotify.framework.MainApplication
import com.andysklyarov.finnotify.framework.receivers.AlarmServiceManager
import com.andysklyarov.finnotify.framework.receivers.ServiceState
import com.andysklyarov.finnotify.interactors.Interactors
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs


class MainViewModel(application: Application) : AndroidViewModel(application) {
    val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    val nowCurrency = ObservableField<CurrencyInRub>()
    val diffCurrency = ObservableField<String>()

    val topLimit = ObservableField<Float>()
    val bottomLimit = ObservableField<Float>()

    val isServiceStarted = ObservableField<Boolean>()
    val isLoading = ObservableField<Boolean>()

    val mainPartsVisibility = ObservableField<Int>()
    val errorVisibility = ObservableField<Int>()

    val backgroundRes = ObservableField<Int>()

    private lateinit var interactors: Interactors
    private lateinit var alarmManager: AlarmServiceManager

    constructor(application: Application, interactors: Interactors) : this(application) {
        initViewModel(interactors);
    }

    fun updateData(currencyCode: String) {
        val res = interactors.getLastDiffCurrency(currencyCode)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoading.set(true) }
            .doFinally { isLoading.set(false) }
            .subscribe({ diffCurrencyInRub ->
                nowCurrency.set(diffCurrencyInRub.currency)

                if (diffCurrencyInRub.diff > 0) diffCurrency.set("+" + diffCurrencyInRub.diff) else diffCurrency.set(
                    diffCurrencyInRub.diff.toString()
                )

                setBackground(diffCurrencyInRub.diff)
                mainPartsVisibility.set(View.VISIBLE)
                errorVisibility.set(View.GONE)

            }) {
                mainPartsVisibility.set(View.GONE)
                errorVisibility.set(View.VISIBLE)
            }
    }

    private fun setBackground(diff: Float) {
        val absoluteDiff = abs(diff)
        if (absoluteDiff < 0.2) {
            backgroundRes.set(R.mipmap.img1)
        } else if (absoluteDiff > 0.2 && absoluteDiff < 0.4) {
            backgroundRes.set(R.mipmap.img2)
        } else {
            backgroundRes.set(R.mipmap.img3)
        }
    }

    fun enableAlarm(hour: Int, minutes: Int, topLimit: Float, bottomLimit: Float) {
        this.topLimit.set(topLimit)
        this.bottomLimit.set(bottomLimit)
        alarmManager.setAlarmTime24(hour, minutes)
        alarmManager.startRepeatingService(nowCurrency.get()!!.name.code, topLimit, bottomLimit)
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
        return getApplication<MainApplication>().getString(R.string.update_on)
    }

    fun getNomPreamble(nom: Int, currencyName: String): String {
        return getApplication<MainApplication>().getString(R.string.nom_string) + " " + nom + " " + currencyName
    }

    private fun initViewModel(interactors: Interactors) {
        val currency = CurrencyInRub(
            CurrencyName(
                getApplication<MainApplication>().getString(R.string.default_currency_name),
                getApplication<MainApplication>().getString(R.string.default_currency_code)
            ),
            LocalDate.of(0, 1, 1),
            0,
            0.0f
        )

        nowCurrency.set(currency)
        diffCurrency.set("0")

        alarmManager = AlarmServiceManager(getApplication())
        updateAlarmState()

        isLoading.set(false)
        mainPartsVisibility.set(View.GONE)
        errorVisibility.set(View.GONE)
        backgroundRes.set(R.mipmap.img0)

        this.interactors = interactors
    }


    private fun updateAlarmState() {
        val state: ServiceState = alarmManager.getServiceState()
        topLimit.set(state.topLimit)
        bottomLimit.set(state.bottomLimit)
        isServiceStarted.set(state.isStarted)
    }

}