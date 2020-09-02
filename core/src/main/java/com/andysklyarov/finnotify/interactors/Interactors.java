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

    public Single<CurrencyInRub> getCurrencyOnDate(String currencyCode, LocalDate date) {
        return currencyRepository.getCurrencyOnDate(currencyCode, date);
    }

    public Single<CurrencyInRub> getLastCurrency(String currencyCode) {
        return getLastDate().flatMap( localDate -> getCurrencyOnDate(currencyCode, localDate));
    }

    public Single<DiffCurrencyInRub> getLastDiffCurrency(String currencyCode) {
        return getLastDate()
                .flatMap(localDate -> getCurrencyOnDate(currencyCode, localDate.minusDays(1))
                        .flatMap(currencyOld -> getCurrencyOnDate(currencyCode, localDate)
                                .map(currencyNew -> new DiffCurrencyInRub(currencyNew, currencyNew.value - currencyOld.value))
                        ));
    }
}