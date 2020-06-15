package com.example.coinstest.interactors;

import com.example.coinstest.data.CurrencyInRubRepository;
import com.example.coinstest.domain.CurrencyInRub;

import java.time.LocalDate;

public final class Interactors {
    private CurrencyInRubRepository currencyRepository;

    public Interactors(CurrencyInRubRepository repository) {
        currencyRepository = repository;
    }

    public LocalDate getLastDate() {
        return currencyRepository.getLastDate();
    }

    public CurrencyInRub getCurrencyOnDate(LocalDate date) {
        return currencyRepository.getCurrencyOnDate(date);
    }

    public CurrencyInRub getLastCurrency() {
        LocalDate lastDate = currencyRepository.getLastDate();
        return currencyRepository.getCurrencyOnDate(lastDate);
    }
}