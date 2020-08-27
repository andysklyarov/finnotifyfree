package com.andysklyarov.finnotify.framework;

import android.app.Application;

import com.andysklyarov.finnotify.data.CurrencyInRubRepository;
import com.andysklyarov.finnotify.interactors.Interactors;

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
