package com.andysklyarov.finnotify.framework

import android.app.Application
import com.andysklyarov.finnotify.data.CurrencyInRubRepository
import com.andysklyarov.finnotify.framework.database.CurrencyDatabase
import com.andysklyarov.finnotify.interactors.Interactors


class MainApplication : Application() {
    private lateinit var repository: CurrencyInRubRepository
    private lateinit var interactors: Interactors

    override fun onCreate() {
        super.onCreate()

        repository = CurrencyInRubRepository(SoapCurrencyInRubDataSource())
        interactors = Interactors(repository)

        ApplicationViewModelFactory.inject(interactors)

        CurrencyDatabase.createDatabase(this)
    }

    fun getInteractors(): Interactors {
        return interactors
    }
}