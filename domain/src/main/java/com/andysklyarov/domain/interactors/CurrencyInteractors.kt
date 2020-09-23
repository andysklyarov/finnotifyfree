package com.andysklyarov.domain.interactors

import com.andysklyarov.domain.model.CurrencyInRub
import io.reactivex.Single
import java.time.LocalDate

interface CurrencyInteractors {
    fun getLastDate(): Single<LocalDate>
    fun getCurrenciesOnDate(date: LocalDate): Single<List<CurrencyInRub>>
    fun getLastCurrencies(): Single<List<CurrencyInRub>>
    fun getLastCurrency(chCode: String): Single<CurrencyInRub>
}