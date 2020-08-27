package com.andysklyarov.finnotify.data;

import com.andysklyarov.finnotify.domain.CurrencyInRub;

import java.time.LocalDate;

public class CurrencyInRubRepository {
    private CurrencyInRubDataSource currencyDataSource;

    public CurrencyInRubRepository(CurrencyInRubDataSource dataSource) {
        currencyDataSource = dataSource;
    }

    public CurrencyInRub getCurrencyOnDate(LocalDate date) {
        return currencyDataSource.getCurrencyOnDate(date);
    }

    public LocalDate getLastDate() {
        return currencyDataSource.getLastDate();
    }
}
