package com.andysklyarov.finnotifyfree.di

import com.andysklyarov.data.repository.CurrencyInRubDBRepository
import com.andysklyarov.data.repository.CurrencyInRubServerRepository
import com.andysklyarov.domain.repository.CurrencyInRubRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Named(CurrencyInRubRepository.SERVER)
    fun provideServerRepository(repository: CurrencyInRubServerRepository): CurrencyInRubRepository {
        return repository
    }


    @Provides
    @Singleton
    @Named(CurrencyInRubRepository.DB)
    fun provideDBRepository(repository: CurrencyInRubDBRepository): CurrencyInRubRepository {
        return repository
    }
}