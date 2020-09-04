package com.andysklyarov.finnotify.data

import com.andysklyarov.finnotify.domain.CurrencyInRub
import io.reactivex.Single
import java.time.LocalDate

interface CurrencyInRubDataSource {
    fun getCurrencyOnDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub>
    fun getLastDate(): Single<LocalDate>
}