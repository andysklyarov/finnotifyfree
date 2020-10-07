package com.andysklyarov.data.repository

import com.andysklyarov.data.database.CurrencyDao
import com.andysklyarov.data.database.CurrencyOnDate
import com.andysklyarov.domain.model.CurrencyInRub
import com.andysklyarov.domain.repository.CurrencyInRubRepository
import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class CurrencyInRubDBRepository @Inject constructor() : CurrencyInRubRepository {
    @Inject
    lateinit var currencyDao: CurrencyDao

    constructor(currencyDao: CurrencyDao) : this() {
        this.currencyDao = currencyDao
    }

    override fun getLastDate(): Single<LocalDate> {
        return Single.fromCallable {
            val dateTime = currencyDao.getLastDateTime()
            val outputFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_TIME_PATTERN)
            return@fromCallable LocalDate.parse(dateTime, outputFormatter)
        }
    }

    override fun getCurrenciesList(date: LocalDate): Single<List<CurrencyInRub>> {
        return Single.fromCallable {
            val currencies = currencyDao.getCurrenciesOnDate(getInputDateTimeString(date))

            return@fromCallable currencies.map { currency: CurrencyOnDate ->
                CurrencyInRub(
                    currency.fullName,
                    currency.chCode,
                    date,
                    currency.nom,
                    currency.value,
                    currency.diff
                )
            }
        }
    }

    override fun insertCurrenciesList(currencies: List<CurrencyInRub>, date: LocalDate) {
        val formattedDate = getInputDateTimeString(date)
        var id = 0L
        currencyDao.insertCurrenciesOnDate(currencies.map { currencyInRub: CurrencyInRub ->
            CurrencyOnDate(
                id++, // to make new recordings use date.toEpochDay() + (id++)
                currencyInRub.fullName,
                currencyInRub.chCode,
                formattedDate,
                currencyInRub.nom,
                currencyInRub.value,
                currencyInRub.diff
            )
        })
    }
}