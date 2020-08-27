package com.andysklyarov.finnotify.domain;

import java.time.LocalDate;

public final class CurrencyInRub {
    public final String name;
    public final LocalDate date;
    public final float value;

    public CurrencyInRub(String name, LocalDate date, float value) {
        this.name = name;
        this.date = date;
        this.value = value;
    }
}