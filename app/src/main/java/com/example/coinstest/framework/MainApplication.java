package com.example.coinstest.framework;

import android.app.Application;

import com.example.coinstest.data.CurrencyInRubRepository;
import com.example.coinstest.interactors.Interactors;

public class MainApplication extends Application {
    private CurrencyInRubRepository repository = null;
    private Interactors interactors = null;

    @Override
    public void onCreate() {
        super.onCreate();

        repository = new CurrencyInRubRepository(new SoapCurrencyInRubDataSource());
        interactors = new Interactors(repository);
        ApplicationViewModelFactory.inject(interactors);


    }

    public Interactors getInteractors() {
        return interactors;
    }
}
