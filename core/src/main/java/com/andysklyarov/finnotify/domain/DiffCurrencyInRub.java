package com.andysklyarov.finnotify.domain;

import java.time.LocalDate;

public class DiffCurrencyInRub extends CurrencyInRub {

    public final float diff;

    public DiffCurrencyInRub(CurrencyName name, LocalDate date, int denomination, float value, float diff) {
        super(name, date, denomination, value);
        this.diff = diff;
    }

    public DiffCurrencyInRub(CurrencyInRub currencyInRub, float diff) {
        super(currencyInRub.name, currencyInRub.date, currencyInRub.denomination, currencyInRub.value);
        this.diff = diff;
    }
}
