package com.andysklyarov.finnotifyfree.data

import com.andysklyarov.finnotifyfree.domain.CurrencyInRub
import io.reactivex.Single
import java.time.LocalDate

interface CurrencyInRubDataSource {
    fun getCurrencyOnDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub>
    fun getCurrencyOnPreviousDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub>
    fun getLastDate(): Single<LocalDate>
}