package com.andysklyarov.finnotify.interactors;

import com.andysklyarov.finnotify.data.CurrencyInRubRepository;
import com.andysklyarov.finnotify.domain.CurrencyInRub;

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