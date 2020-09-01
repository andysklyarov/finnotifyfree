package com.andysklyarov.finnotify.data;

import com.andysklyarov.finnotify.domain.CurrencyInRub;

import java.time.LocalDate;

import io.reactivex.Single;

public class CurrencyInRubRepository {
    private CurrencyInRubDataSource currencyDataSource;

    public CurrencyInRubRepository(CurrencyInRubDataSource dataSource) {
        currencyDataSource = dataSource;
    }

    public Single<CurrencyInRub> getCurrencyOnDate(LocalDate date) {
        return currencyDataSource.getCurrencyOnDate(date);
    }

    public Single<LocalDate> getLastDate() {
        return currencyDataSource.getLastDate();
    }
}
