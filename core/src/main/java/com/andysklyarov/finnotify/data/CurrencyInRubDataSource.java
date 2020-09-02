package com.andysklyarov.finnotify.data;

import com.andysklyarov.finnotify.domain.CurrencyInRub;

import java.time.LocalDate;

import io.reactivex.Single;

public interface CurrencyInRubDataSource {

    Single<CurrencyInRub> getCurrencyOnDate(String currencyCode, LocalDate date);

    Single<LocalDate> getLastDate();
}