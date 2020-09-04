package com.andysklyarov.finnotify.framework.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CurrencyOnDate::class, LastDate::class], version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun getCurrencyDao(): CurrencyDao
}