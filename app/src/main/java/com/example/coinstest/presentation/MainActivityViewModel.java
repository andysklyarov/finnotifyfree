package com.example.coinstest.presentation;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.coinstest.domain.CurrencyInRub;
import com.example.coinstest.interactors.Interactors;

public class MainActivityViewModel extends ViewModel {

    public final ObservableField<CurrencyInRub> currencyField = new ObservableField<>();
    private Interactors interactors;

    public MainActivityViewModel(Interactors interactors) {
        CurrencyInRub currency = new CurrencyInRub("Empty", "Empty", 0.0f);
        currencyField.set(currency);
        this.interactors = interactors;
    }

    public void changeCurrencyInRub() {
        currencyField.set(interactors.getLastCurrency());
    }

    public void onResume() {
        changeCurrencyInRub();
    }
}


