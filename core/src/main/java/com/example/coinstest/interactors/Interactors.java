package com.example.coinstest.interactors;

import com.example.coinstest.data.CurrencyInRubRepository;
import com.example.coinstest.domain.CurrencyInRub;

public final class Interactors {
    private CurrencyInRubRepository currencyRepository;

    public Interactors(CurrencyInRubRepository repository) {
        currencyRepository = repository;
    }

    public String getLastDate() {
        return currencyRepository.getLastDate();
    }

    public CurrencyInRub getCurrencyOnDate(String date) {
        return currencyRepository.getCurrencyOnDate(date);
    }

    public CurrencyInRub getLastCurrency() {
        String lastDate = currencyRepository.getLastDate();
        return currencyRepository.getCurrencyOnDate(lastDate);
    }
}