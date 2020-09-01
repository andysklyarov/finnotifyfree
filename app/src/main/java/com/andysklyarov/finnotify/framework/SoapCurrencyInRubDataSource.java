package com.andysklyarov.finnotify.framework;

import com.andysklyarov.finnotify.data.CurrencyInRubDataSource;
import com.andysklyarov.finnotify.domain.CurrencyInRub;
import com.andysklyarov.finnotify.framework.soap.SoapCbrUtils;

import java.time.LocalDate;

import io.reactivex.Single;


public class SoapCurrencyInRubDataSource implements CurrencyInRubDataSource {

    @Override
    public Single<CurrencyInRub> getCurrencyOnDate(LocalDate date) {
        return SoapCbrUtils.getCurrencyOnDate("USD", date);
    }

    @Override
    public Single<LocalDate> getLastDate() {
        return SoapCbrUtils.getLastServerDate();
    }
}