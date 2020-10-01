package com.andysklyarov.domain.interactors

import com.andysklyarov.domain.hasNetworkException
import com.andysklyarov.domain.model.CurrencyInRub
import com.andysklyarov.domain.repository.CurrencyInRubRepository
import io.reactivex.Single
import org.threeten.bp.LocalDate
import javax.inject.Inject
import javax.inject.Named

class CurrencyInteractorsImpl @Inject constructor() : CurrencyInteractors {

    @Inject
    @Named(CurrencyInRubRepository.SERVER)
    lateinit var serverRepository: CurrencyInRubRepository

    @Inject
    @Named(CurrencyInRubRepository.DB)
    lateinit var dbRepository: CurrencyInRubRepository

    override fun getLastDate(): Single<LocalDate> {
        return serverRepository.getLastDate().onErrorReturn {
            if (hasNetworkException(it)) {
                dbRepository.getLastDate().blockingGet() //todo avoid blockingGet
            } else {
                null //todo handle error
            }
        }
    }

    override fun getCurrenciesOnDate(date: LocalDate): Single<List<CurrencyInRub>> {
        return serverRepository.getCurrenciesList(date)
            .doOnSuccess { currencies: List<CurrencyInRub> ->
                dbRepository.insertCurrenciesList(currencies, date)
            }
            .onErrorReturn {
                if (hasNetworkException(it)) {
                    dbRepository.getCurrenciesList(date).blockingGet() //todo avoid blockingGet
                } else {
                    null //todo handle error
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
            it.filter { currencyInRub -> currencyInRub.chCode == chCode }[0]
        }
    }
}