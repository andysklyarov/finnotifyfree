package com.andysklyarov.finnotify.framework.database

import android.app.Application
import androidx.room.Room


object CurrencyDatabase {
    const val DATABASE_NAME = "currency_database"
    private lateinit var database: DataBase

    fun createDatabase(context: Application) {

//        context.deleteDatabase(DATABASE_NAME);
        database = Room.databaseBuilder(context, DataBase::class.java, DATABASE_NAME)
            .allowMainThreadQueries()
            .build()
    }

    fun getDatabase(): DataBase {
        return database
    }
}