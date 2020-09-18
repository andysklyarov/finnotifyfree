package com.andysklyarov.finnotifyfree.di

import com.andysklyarov.data.soap.SoapCbrApi
import com.andysklyarov.domain.usecases.CurrencyUsecase
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.alarm.AlarmReceiver
import com.andysklyarov.finnotifyfree.ui.CurrencyFragment
import dagger.BindsInstance
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

    fun geCurrencyUsecase(): CurrencyUsecase

    fun getReceiver(): AlarmReceiver

    fun injectCurrencyFragment(injector: CurrencyFragment)
}