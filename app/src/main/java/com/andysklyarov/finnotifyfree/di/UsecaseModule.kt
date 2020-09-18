package com.andysklyarov.finnotifyfree.di

import com.andysklyarov.domain.usecases.CurrencyUsecase
import com.andysklyarov.domain.usecases.CurrencyUsecaseImplementation
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UsecaseModule {

    @Provides
    @Singleton
    fun provideUsecase(impl : CurrencyUsecaseImplementation): CurrencyUsecase {
        return impl
    }
}