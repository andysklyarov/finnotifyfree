package com.andysklyarov.finnotify.framework;

import com.andysklyarov.finnotify.data.CurrencyInRubDataSource;
import com.andysklyarov.finnotify.domain.CurrencyInRub;
import com.andysklyarov.finnotify.framework.soap.SoapCbrWebService;

import java.time.LocalDate;


public class SoapCurrencyInRubDataSource implements CurrencyInRubDataSource {

    private SoapCbrWebService webService = new SoapCbrWebService();

    @Override
    public CurrencyInRub getCurrencyOnDate(LocalDate date) {
        return webService.getCurrencyRetrofit("USD", date);
//        return webService.getCurrency("USD", date);
    }

    @Override
    public LocalDate getLastDate() {

        return webService.getLastServerDateRetrofit();
//        return webService.getLastServerDate();
    }
}
