package com.andysklyarov.finnotify.framework.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrencyOnDate(currency: CurrencyOnDate)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrenciesOnDate(currencies: List<CurrencyOnDate>)

    @Query("select * from CurrencyOnDate where DateTime = :dateTime")
    fun getCurrenciesOnDate(dateTime: String): List<CurrencyOnDate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastDate(currency: LastDate)

    @Query("select * from LastDate")
    fun getLastDatesList(): List<LastDate>

    @Query("select * from LastDate order by id desc limit 1")
    fun getLastDate(): LastDate
}