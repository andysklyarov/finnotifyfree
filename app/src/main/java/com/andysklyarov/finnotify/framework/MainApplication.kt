package com.andysklyarov.finnotify.framework

import android.app.Application
import androidx.preference.PreferenceManager
import com.andysklyarov.finnotify.R
import com.andysklyarov.finnotify.data.CurrencyInRubRepository
import com.andysklyarov.finnotify.framework.database.CurrencyDatabase
import com.andysklyarov.finnotify.interactors.Interactors

private const val SAVED_RES_SHARED_PREFERENCES_KEY = "SAVED_RES_SHARED_PREFERENCES_KEY"
private const val SAVED_CODE_SHARED_PREFERENCES_KEY = "SAVED_CODE_SHARED_PREFERENCES_KEY"

class MainApplication : Application() {
    private lateinit var repository: CurrencyInRubRepository
    private lateinit var interactors: Interactors

    override fun onCreate() {
        super.onCreate()

        repository = CurrencyInRubRepository(SoapCurrencyInRubDataSource())
        interactors = Interactors(repository)

        ApplicationViewModelFactory.inject(interactors)

        CurrencyDatabase.createDatabase(this)
    }

    fun getInteractors(): Interactors {
        return interactors
    }

    fun loadImgRes(): Int { // todo migrate to Room
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return myPreferences.getInt(
            SAVED_RES_SHARED_PREFERENCES_KEY,
            R.mipmap.img0
        )
    }

    fun saveImgRes(imgRes: Int) { // todo migrate to Room
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val myEditor = myPreferences.edit()
        myEditor.putInt(SAVED_RES_SHARED_PREFERENCES_KEY, imgRes)
        myEditor.apply()
    }

    fun loadCode(): String { // todo migrate to Room
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        return myPreferences.getString(
            SAVED_CODE_SHARED_PREFERENCES_KEY,
            getString(R.string.default_currency_code)
        )!!
    }

    fun saveCode(code: String) { // todo migrate to Room
        val myPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val myEditor = myPreferences.edit()
        myEditor.putString(SAVED_CODE_SHARED_PREFERENCES_KEY, code)
        myEditor.apply()
    }
}