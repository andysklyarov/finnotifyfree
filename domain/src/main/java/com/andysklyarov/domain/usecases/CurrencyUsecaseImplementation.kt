package com.andysklyarov.domain.usecases

import com.andysklyarov.domain.hasNetworkException
import com.andysklyarov.domain.model.CurrencyInRub
import com.andysklyarov.domain.repository.CurrencyInRubRepository
import io.reactivex.Single
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

class CurrencyUsecaseImplementation @Inject constructor() : CurrencyUsecase {

    @Inject
    @Named(CurrencyInRubRepository.SERVER)
    lateinit var serverRepository: CurrencyInRubRepository

    @Inject
    @Named(CurrencyInRubRepository.DB)
    lateinit var dbRepository: CurrencyInRubRepository

    override fun getLastDate(): Single<LocalDate> {
        return serverRepository.getLastDate().onErrorReturn { t ->
            if (hasNetworkException(t)) {
                dbRepository.getLastDate().blockingGet() //todo avoid blockingGet
            } else {
                null
            }
        }
    }

    override fun getCurrenciesOnDate(date: LocalDate): Single<List<CurrencyInRub>> {
        return serverRepository.getCurrenciesList(date)
            .doOnSuccess { currencies: List<CurrencyInRub> ->
                dbRepository.insertCurrenciesList(currencies, date)
            }
            .onErrorReturn { t ->
                if (hasNetworkException(t)) {
                    dbRepository.getCurrenciesList(date).blockingGet() //todo avoid blockingGet
                } else {
                    null
                }
            }
    }

    override fun getLastCurrencies(): Single<List<CurrencyInRub>> {
        return getLastDate().flatMap {
            getCurrenciesOnDate(it)
        }
    }

    override fun getLastCurrency(chCode: String): Single<CurrencyInRub> {
        return getLastCurrencies().map {
            it.filter {
                    currencyInRub -> currencyInRub.chCode == chCode }[0]
        }
    }
}