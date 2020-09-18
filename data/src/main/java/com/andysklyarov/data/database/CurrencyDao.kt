package com.andysklyarov.data.database

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

    @Query("select * from CurrencyOnDate where DateTime = :dateTime and ChCode = :chCode")
    fun getCurrencyOnDate(chCode: String, dateTime: String): CurrencyOnDate

    @Query("select DateTime from CurrencyOnDate order by id desc limit 1")
    fun getLastDateTime(): String
}