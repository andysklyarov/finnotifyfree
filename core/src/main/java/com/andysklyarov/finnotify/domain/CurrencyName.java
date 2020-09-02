package com.andysklyarov.finnotify.domain;

public class CurrencyName {
    public final String fullName;
    public final String code;

    public CurrencyName(String fullName, String code) {
        this.fullName = fullName;
        this.code = code;
    }
}
