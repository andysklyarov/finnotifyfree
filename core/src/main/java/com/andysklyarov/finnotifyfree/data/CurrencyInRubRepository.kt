package com.andysklyarov.finnotifyfree.data

import com.andysklyarov.finnotifyfree.domain.CurrencyInRub
import io.reactivex.Single
import java.time.LocalDate

class CurrencyInRubRepository(private val currencyDataSource: CurrencyInRubDataSource) {

    fun getCurrencyOnDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub> {
        return currencyDataSource.getCurrencyOnDate(currencyCode, date)
    }

    fun getCurrencyOnPreviousDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub> {
        return currencyDataSource.getCurrencyOnPreviousDate(currencyCode, date)
    }

    fun getLastDate(): Single<LocalDate>{
        return currencyDataSource.getLastDate()
    }
}