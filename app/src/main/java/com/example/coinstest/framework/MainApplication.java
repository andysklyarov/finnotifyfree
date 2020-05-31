package com.example.coinstest.framework;

import android.app.Application;

import com.example.coinstest.data.CurrencyInRubRepository;
import com.example.coinstest.interactors.Interactors;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        CurrencyInRubRepository repository = new CurrencyInRubRepository(new SoapCurrencyInRubDataSource());
        Interactors interactors = new Interactors(repository);
        ApplicationViewModelFactory.inject(interactors);
    }
}
