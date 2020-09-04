package com.andysklyarov.finnotify.framework

import com.andysklyarov.finnotify.data.CurrencyInRubDataSource
import com.andysklyarov.finnotify.domain.CurrencyInRub
import com.andysklyarov.finnotify.framework.soap.SoapCbrUtils
import io.reactivex.Single
import java.time.LocalDate

class SoapCurrencyInRubDataSource : CurrencyInRubDataSource {
    override fun getCurrencyOnDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub> {
        return SoapCbrUtils.getCurrencyOnDate(currencyCode, date)
    }

    override fun getLastDate(): Single<LocalDate> {
        return SoapCbrUtils.getLastServerDate()
    }
}