package com.andysklyarov.finnotifyfree.ui

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import com.andysklyarov.domain.interactors.CurrencyInteractors
import com.andysklyarov.domain.model.CurrencyInRub
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.R
import com.andysklyarov.finnotifyfree.alarm.AlarmServiceManager
import com.andysklyarov.finnotifyfree.alarm.ServiceState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDate
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.math.abs

class MainViewModel @Inject constructor(application: AppDelegate) : AndroidViewModel(application) {
    val currency = ObservableField<CurrencyInRub>()
    val topLimit = ObservableFloat()
    val bottomLimit = ObservableFloat()

    val isServiceStarted = ObservableBoolean()
    val isLoading = ObservableBoolean()
    val isError = ObservableBoolean()
    val backgroundRes = ObservableInt()

    private var disposable: Disposable? = null

    private var alarmManager: AlarmServiceManager

    @Inject
    lateinit var usecase: CurrencyInteractors


    init {
        currency.set(
            CurrencyInRub(
                getApplication<AppDelegate>().getString(R.string.default_currency_name),
                getApplication<AppDelegate>().getString(R.string.default_currency_code),
                LocalDate.of(1, 1, 1),
                0,
                0.0f,
                0.0f
            )
        )
        alarmManager = AlarmServiceManager(getApplication())
        updateAlarmState()
        isLoading.set(false)
        isError.set(false)

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
                setBackground(it.diff)
                isError.set(false)
            }) {
                isError.set(true)
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