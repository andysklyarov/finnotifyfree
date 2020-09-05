package com.andysklyarov.finnotifyfree.framework

import com.andysklyarov.finnotifyfree.data.CurrencyInRubDataSource
import com.andysklyarov.finnotifyfree.domain.CurrencyInRub
import com.andysklyarov.finnotifyfree.framework.soap.SoapCbrUtils
import io.reactivex.Single
import java.time.LocalDate

class SoapCurrencyInRubDataSource : CurrencyInRubDataSource {
    override fun getCurrencyOnDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub> {
        return SoapCbrUtils.getCurrencyOnDate(currencyCode, date)
    }

    override fun getCurrencyOnPreviousDate(
        currencyCode: String,
        date: LocalDate
    ): Single<CurrencyInRub> {
        return SoapCbrUtils.getCurrencyOnPreviousDate(currencyCode, date)
    }

    override fun getLastDate(): Single<LocalDate> {
        return SoapCbrUtils.getLastServerDate()
    }
}