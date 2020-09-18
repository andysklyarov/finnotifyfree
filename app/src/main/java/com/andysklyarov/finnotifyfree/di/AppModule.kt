package com.andysklyarov.finnotifyfree.di

import androidx.room.Room
import com.andysklyarov.data.database.CurrencyDao
import com.andysklyarov.data.database.DataBase
import com.andysklyarov.finnotifyfree.AppDelegate
import com.andysklyarov.finnotifyfree.alarm.AlarmReceiver
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
class AppModule(app: AppDelegate) {
    val mApp: AppDelegate = app

    @Provides
    @Singleton
    fun provideApp(): AppDelegate {
        return mApp;
    }

    @Provides
    @Singleton
    fun provideDatabase(): DataBase {
        return Room.databaseBuilder(mApp, DataBase::class.java, "currency_database")
            .fallbackToDestructiveMigration() // уничтожит все данные пользователя
            .build()
    }

    @Provides
    @Singleton
    fun provideDao(database: DataBase): CurrencyDao {
        return database.getCurrencyDao();
    }
}