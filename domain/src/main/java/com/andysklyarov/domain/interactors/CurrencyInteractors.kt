package com.andysklyarov.domain.interactors

import com.andysklyarov.domain.model.CurrencyInRub
import io.reactivex.Single
import org.threeten.bp.LocalDate

interface CurrencyInteractors {
    fun getLastDate(): Single<LocalDate>
    fun getCurrenciesOnDate(date: LocalDate): Single<List<CurrencyInRub>>
    fun getLastCurrencies(): Single<List<CurrencyInRub>>
    fun getLastCurrency(chCode: String): Single<CurrencyInRub>
}