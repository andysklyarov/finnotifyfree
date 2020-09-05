package com.andysklyarov.finnotify.interactors

import com.andysklyarov.finnotify.data.CurrencyInRubRepository
import com.andysklyarov.finnotify.domain.CurrencyInRub
import com.andysklyarov.finnotify.domain.DiffCurrencyInRub
import io.reactivex.Single
import java.time.LocalDate

class Interactors(private val currencyInRubRepository: CurrencyInRubRepository) {
    fun getLastDate(): Single<LocalDate> {
        return currencyInRubRepository.getLastDate()
    }

    fun getCurrencyOnDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub> {
        return currencyInRubRepository.getCurrencyOnDate(currencyCode, date)
    }

    fun getCurrencyOnPreviousDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub> {
        return currencyInRubRepository.getCurrencyOnPreviousDate(currencyCode, date)
    }

    fun getLastCurrency(currencyCode: String): Single<CurrencyInRub> {
        return getLastDate().flatMap { localDate -> getCurrencyOnDate(currencyCode, localDate) }
    }

    fun getLastDiffCurrency(currencyCode: String): Single<DiffCurrencyInRub> {
        return getLastDate()
            .flatMap { localDate ->
                getCurrencyOnPreviousDate(currencyCode, localDate) //todo avoid duct tape
                    .flatMap { currencyOld ->
                        getCurrencyOnDate(currencyCode, localDate)
                            .map { currencyNew: CurrencyInRub ->
                                DiffCurrencyInRub(
                                    currencyNew,
                                    currencyNew.value - currencyOld.value
                                )
                            }
                    }
            }
    }
}