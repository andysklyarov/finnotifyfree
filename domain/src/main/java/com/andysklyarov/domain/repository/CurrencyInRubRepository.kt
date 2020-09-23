package com.andysklyarov.domain.repository

import com.andysklyarov.domain.model.CurrencyInRub
import io.reactivex.Single
import java.time.LocalDate


interface CurrencyInRubRepository {

    companion object {
        const val SERVER = "SERVER"
        const val DB = "DB"
    }

    fun getLastDate(): Single<LocalDate>
    fun getCurrenciesList(date: LocalDate): Single<List<CurrencyInRub>>
    fun insertCurrenciesList(currencies: List<CurrencyInRub>, date: LocalDate)
}