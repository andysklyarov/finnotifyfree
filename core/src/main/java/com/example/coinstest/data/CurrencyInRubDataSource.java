package com.example.coinstest.data;

import com.example.coinstest.domain.CurrencyInRub;

import java.time.LocalDate;

public interface CurrencyInRubDataSource {
    CurrencyInRub getCurrencyOnDate(LocalDate date);

    LocalDate getLastDate();
}
