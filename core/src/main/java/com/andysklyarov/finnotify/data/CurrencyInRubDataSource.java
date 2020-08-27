package com.andysklyarov.finnotify.data;

import com.andysklyarov.finnotify.domain.CurrencyInRub;

import java.time.LocalDate;

public interface CurrencyInRubDataSource {
    CurrencyInRub getCurrencyOnDate(LocalDate date);

    LocalDate getLastDate();
}
