package com.andysklyarov.finnotify.domain;

import java.time.LocalDate;

public class CurrencyInRub {
    public final CurrencyName name;
    public final LocalDate date;
    public final int denomination;
    public final float value;

    public CurrencyInRub(CurrencyName name, LocalDate date, int denomination, float value) {
        this.name = name;
        this.date = date;
        this.denomination = denomination;
        this.value = value;
    }
}