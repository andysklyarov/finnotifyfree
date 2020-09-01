package com.andysklyarov.finnotify.interactors;

import com.andysklyarov.finnotify.data.CurrencyInRubRepository;
import com.andysklyarov.finnotify.domain.CurrencyInRub;
import com.andysklyarov.finnotify.domain.DiffCurrencyInRub;

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
        return getLastDate().flatMap(this::getCurrencyOnDate);
    }

    public Single<DiffCurrencyInRub> getLastDiffCurrency() {
        return getLastDate()
                .flatMap(localDate -> getCurrencyOnDate(localDate.minusDays(1))
                        .flatMap(currencyOld -> getCurrencyOnDate(localDate)
                                .map(currencyNew -> new DiffCurrencyInRub(currencyNew, currencyNew.value - currencyOld.value))
                        ));
    }
}