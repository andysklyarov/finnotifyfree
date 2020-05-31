package com.example.coinstest.data;

import com.example.coinstest.domain.CurrencyInRub;

public class CurrencyInRubRepository {
    private CurrencyInRubDataSource currencyDataSource;

    public CurrencyInRubRepository(CurrencyInRubDataSource dataSource) {
        currencyDataSource = dataSource;
    }

    public CurrencyInRub getCurrencyOnDate(String date) {
        return currencyDataSource.getCurrencyOnDate(date);
    }

    public String getLastDate() {
        return currencyDataSource.getLastDate();
    }
}
