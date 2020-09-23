package com.andysklyarov.finnotifyfree.di

import com.andysklyarov.domain.interactors.CurrencyInteractors
import com.andysklyarov.domain.interactors.CurrencyInteractorsImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UsecaseModule {

    @Provides
    @Singleton
    fun provideUsecase(impl: CurrencyInteractorsImpl): CurrencyInteractors {
        return impl
    }
}