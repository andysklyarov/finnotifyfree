package com.example.coinstest.data;

import com.example.coinstest.domain.CurrencyInRub;

public interface CurrencyInRubDataSource {
    CurrencyInRub getCurrencyOnDate(String date);

    String getLastDate();
}
