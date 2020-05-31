package com.example.coinstest.framework;

import com.example.coinstest.data.CurrencyInRubDataSource;
import com.example.coinstest.domain.CurrencyInRub;
import com.example.coinstest.framework.soap.SoapCbrWebService;


public class SoapCurrencyInRubDataSource implements CurrencyInRubDataSource {

    private SoapCbrWebService webService = new SoapCbrWebService();

    @Override
    public CurrencyInRub getCurrencyOnDate(String date) {
        return webService.getCurrency("USD", date);
    }

    @Override
    public String getLastDate() {
        return webService.getLastServerDate();
    }
}
