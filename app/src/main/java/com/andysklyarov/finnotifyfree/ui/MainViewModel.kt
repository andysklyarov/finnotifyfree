package com.andysklyarov.finnotifyfree.ui

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.preference.PreferenceManager
import com.andysklyarov.domain.interactors.CurrencyInteractors
import com.andysklyarov.domain.model.CurrencyInRub
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.R
import com.andysklyarov.finnotifyfree.alarm.AlarmServiceManager
import com.andysklyarov.finnotifyfree.ui.fragments.SEEK_BAR_MAX_VALUE
import com.andysklyarov.finnotifyfree.ui.fragments.SettingsFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import javax.inject.Inject
import kotlin.math.abs

class MainViewModel @Inject constructor(private val app: AppDelegate) :
    AndroidViewModel(app) {

    companion object {
        const val MAX_ABS_DYNAMICS_DEFAULT_VALUE = 1.0f
    }

    val currency = ObservableField<CurrencyInRub>()
    val isLoading = ObservableBoolean()
    val isError = ObservableBoolean()
    val backgroundRes = ObservableInt()


    private var currencyCode: String = "USD"
    private var lowDynamics: Float = 0f
    private var highDynamics: Float = 0f

    private var disposable: Disposable? = null

    @Inject
    lateinit var usecase: CurrencyInteractors


    init {
        val defaultCurrencyName = app.getString(R.string.default_currency_name)
        val defaultCurrencyCode = app.getString(R.string.default_currency_code)
        val date = LocalDate.of(1, 1, 1)
        currency.set(CurrencyInRub(defaultCurrencyName, defaultCurrencyCode, date, 0, 0.0f, 0.0f))
        isLoading.set(false)
        isError.set(false)

        val imgResId = app.loadImgRes()
        backgroundRes.set(imgResId)
    }

    fun updateSettings() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
        currencyCode = sharedPreferences?.getString(
            app.getString(R.string.currency_code_string_key),
            app.getString(R.string.default_currency_code)
        ) ?: app.getString(R.string.default_currency_code)

        val baseValue =
            sharedPreferences.getString(app.getString(R.string.max_abs_dynamics_string_key), null)
                ?.toFloat() ?: MAX_ABS_DYNAMICS_DEFAULT_VALUE
        val lowDynamicsPercent =
            sharedPreferences.getInt(app.getString(R.string.low_dynamics_int_key), 0).toFloat()
        val highDynamicsPercent =
            sharedPreferences.getInt(app.getString(R.string.high_dynamics_int_key), 0).toFloat()
        lowDynamics = baseValue * (lowDynamicsPercent / SEEK_BAR_MAX_VALUE)
        highDynamics = baseValue * (highDynamicsPercent / SEEK_BAR_MAX_VALUE)
    }

    fun updateData() {
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

    private fun setBackground(diff: Float) {
        val absoluteDiff = abs(diff)
        if (absoluteDiff < lowDynamics) {
            backgroundRes.set(R.mipmap.img1)
        } else if (absoluteDiff > lowDynamics && absoluteDiff < highDynamics) {
            backgroundRes.set(R.mipmap.img2)
        } else {
            backgroundRes.set(R.mipmap.img3)
        }
    }
}