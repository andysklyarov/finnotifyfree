package com.andysklyarov.finnotify.interactors;

import com.andysklyarov.finnotify.data.CurrencyInRubRepository;
import com.andysklyarov.finnotify.domain.CurrencyInRub;

import io.reactivex.Single;

import java.time.LocalDate;

public final class Interactors {
    private CurrencyInRubRepository currencyRepository;

    public Interactors(CurrencyInRubRepository repository) {
        currencyRepository = repository;
    }

    public Single<LocalDate> getLastDate() {
        return currencyRepository.getLastDate();
    }

    public Single<CurrencyInRub> getCurrencyOnDate(LocalDate date) {
        return currencyRepository.getCurrencyOnDate(date);
    }

    public Single<CurrencyInRub> getLastCurrency() {
        return getLastDate().flatMap(localDate -> getCurrencyOnDate(localDate));
    }
}