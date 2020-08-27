package com.andysklyarov.finnotify.framework.soap;

import com.andysklyarov.finnotify.domain.CurrencyInRub;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.CursOnDateAsyncTask;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseValuteCursOnDate;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.LatestDateTimeCbrAsyncTask;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.Float.parseFloat;

public final class SoapCbrWebService {

    private static final String INPUT_DATE_TIME_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "T00:00:00";
    public static final String OUTPUT_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    public CurrencyInRub getCurrencyRetrofit(String currencyName, LocalDate date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(INPUT_DATE_TIME_PATTERN);
        String formattedDate = inputFormatter.format(date) + TIME_PATTERN;

        CursOnDateAsyncTask asyncTask = new CursOnDateAsyncTask(formattedDate);

        float currencyValueInRub = 0;
        List<ResponseValuteCursOnDate> currencies = null;
        try {
            currencies = asyncTask.execute().get();
            if(currencies != null) {
                for (ResponseValuteCursOnDate currency : currencies) {
                    if (currency.chCode.equals(currencyName)) {
                        currencyValueInRub = parseFloat(currency.curs);
                        break;
                    }
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return new CurrencyInRub(currencyName, date, currencyValueInRub);
    }

    public LocalDate getLastServerDateRetrofit() {
        LatestDateTimeCbrAsyncTask soapCbrAsyncTaskRetrofit = new LatestDateTimeCbrAsyncTask();

        String rawDateTime = null;
        try {
            rawDateTime = soapCbrAsyncTaskRetrofit.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_TIME_PATTERN);
        return LocalDate.parse(rawDateTime, outputFormatter); //"2020-04-30T00:00:00"
    }
}
