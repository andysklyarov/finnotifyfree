package com.andysklyarov.finnotifyfree.di

import com.andysklyarov.data.network.SoapCbrApi
import com.andysklyarov.domain.interactors.CurrencyInteractors
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.alarm.AlarmReceiver
import com.andysklyarov.finnotifyfree.ui.MainActivity
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        UsecaseModule::class,
        ViewModelModule::class,
        ReceiverModule::class,
        AndroidSupportInjectionModule::class]
)
interface AppComponent : AndroidInjector<AppDelegate> {

    fun getSoapCbrApi(): SoapCbrApi

    fun getApp(): AppDelegate

    fun geCurrencyUsecase(): CurrencyInteractors

    fun getReceiver(): AlarmReceiver

    fun injectActivity(injector: MainActivity)
}