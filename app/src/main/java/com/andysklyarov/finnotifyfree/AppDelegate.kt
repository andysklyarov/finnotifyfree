package com.andysklyarov.finnotifyfree

import android.app.Application
import androidx.preference.PreferenceManager
import com.andysklyarov.finnotifyfree.di.AppComponent
import com.andysklyarov.finnotifyfree.di.AppModule
import com.andysklyarov.finnotifyfree.di.DaggerAppComponent
import com.andysklyarov.finnotifyfree.di.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

private const val SAVED_RES_SHARED_PREFERENCES_KEY = "SAVED_RES_SHARED_PREFERENCES_KEY"
private const val SAVED_CODE_SHARED_PREFERENCES_KEY = "SAVED_CODE_SHARED_PREFERENCES_KEY"

class AppDelegate : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    companion object {
        private lateinit var appComponent: AppComponent
        fun getAppComponent(): AppComponent {
            return appComponent
        }
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .build()
        appComponent.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector
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

    fun loadPreviousChCode(): String { // todo migrate to Room
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