package com.andysklyarov.finnotify.framework.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCurrencyOnDate(CurrencyOnDate currency);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCurrenciesOnDate(List<CurrencyOnDate> currencies);

    @Query("select * from CurrencyOnDate where DateTime = :dateTime")
    List<CurrencyOnDate> getCurrenciesOnDate(String dateTime);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLastDate(LastDate currency);

    @Query("select * from LastDate")
    List<LastDate> getLastDatesList();

    @Query("select * from LastDate order by id desc limit 1")
    LastDate getLastDate();
}
