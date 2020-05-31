package com.example.coinstest.domain;

public final class CurrencyInRub {
    public final String name;
    public final String date;
    public final float value;

    public CurrencyInRub(String name, String date, float value) {
        this.name = name;
        this.date = date;
        this.value = value;
    }
}