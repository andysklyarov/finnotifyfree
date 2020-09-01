package com.andysklyarov.finnotify.framework.soap;

import androidx.annotation.NonNull;

import com.andysklyarov.finnotify.domain.CurrencyInRub;
import com.andysklyarov.finnotify.framework.database.CurrencyDao;
import com.andysklyarov.finnotify.framework.database.CurrencyOnDate;
import com.andysklyarov.finnotify.framework.database.DbUtils;
import com.andysklyarov.finnotify.framework.database.LastDate;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request.RequestGetCursOnDateXMLBody;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request.RequestGetCursOnDateXMLData;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request.RequestGetCursOnDateXMLEnvelope;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseGetCursOnDateXMLBody;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseGetCursOnDateXMLEnvelope;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseGetCursOnDateXMLResponse;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseGetCursOnDateXMLResult;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseValuteCursOnDate;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseValuteData;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.request.RequestLatestDateTimeBody;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.request.RequestLatestDateTimeData;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.request.RequestLatestDateTimeEnvelope;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.response.ResponseLatestDateTimeBody;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.response.ResponseLatestDateTimeData;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.response.ResponseLatestDateTimeEnvelope;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static java.lang.Float.parseFloat;

public final class SoapCbrUtils {

    private static final String INPUT_DATE_TIME_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "T00:00:00";
    private static final String OUTPUT_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String INTERNAL_DATE_PATTERN = "yyyyMMdd";

    public static Single<LocalDate> getLastServerDate() {

        RequestLatestDateTimeEnvelope envelope = new RequestLatestDateTimeEnvelope();
        RequestLatestDateTimeBody body = new RequestLatestDateTimeBody();
        RequestLatestDateTimeData data = new RequestLatestDateTimeData();

        body.setLatestDateTimeData(data);
        envelope.setBody(body);

        return HttpUtils.getApiService()
                .getLatestDateTime(envelope)
                .doOnSuccess(response -> writeLastDateToDb(response))
                .onErrorReturn(throwable -> getLastDateFromDb(throwable))
                .map(response -> {
                    String dateTime = response.getBody()
                            .getLatestDateTimeData()
                            .getDateTime();

                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_TIME_PATTERN);
                    return LocalDate.parse(dateTime, outputFormatter); //"2020-04-30T00:00:00"
                });
    }

    public static Single<CurrencyInRub> getCurrencyOnDate(String currencyName, LocalDate date) {
        RequestGetCursOnDateXMLEnvelope envelope = new RequestGetCursOnDateXMLEnvelope();
        RequestGetCursOnDateXMLBody body = new RequestGetCursOnDateXMLBody();
        RequestGetCursOnDateXMLData data = new RequestGetCursOnDateXMLData();

        String formattedDate = getInputDateTimeString(date);

        data.setDateTime(formattedDate);
        body.setGetCursOnDateXML(data);
        envelope.setBody(body);

        return HttpUtils.getApiService()
                .getCursOnDateXML(envelope)
                .doOnSuccess(response -> writeCurrencyToDb(formattedDate, response))
                .onErrorReturn(throwable -> getCurrencyFromDbOrNull(date, throwable))
                .map(response -> getCurrencyInRub(currencyName, response));
    }

    private static void writeLastDateToDb(ResponseLatestDateTimeEnvelope response) {
        CurrencyDao dao = DbUtils.getDatabase().getCurrencyDao();

        String dateTime = response.getBody()
                .getLatestDateTimeData()
                .getDateTime();

        dao.insertLastDate(new LastDate(0, dateTime));
    }

    private static ResponseLatestDateTimeEnvelope getLastDateFromDb(Throwable throwable) {
        if (hasNetworkException(throwable)) {
            CurrencyDao dao = DbUtils.getDatabase().getCurrencyDao();

            ResponseLatestDateTimeEnvelope response = new ResponseLatestDateTimeEnvelope();
            ResponseLatestDateTimeBody body = new ResponseLatestDateTimeBody();
            ResponseLatestDateTimeData data = new ResponseLatestDateTimeData();

            String date = dao.getLastDate().getDate();

            data.setDateTime(date);
            body.setLatestDateTimeData(data);
            response.setBody(body);

            return response;
        } else {
            return null;
        }
    }

    @NonNull
    private static String getInputDateTimeString(LocalDate date) {
        return getFormattedDateString(date, INPUT_DATE_TIME_PATTERN) + TIME_PATTERN;
    }

    @NonNull
    private static String getFormattedDateString(LocalDate date, String format) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(format);
        return inputFormatter.format(date);
    }

    private static void writeCurrencyToDb(String formattedDate, ResponseGetCursOnDateXMLEnvelope response) {
        CurrencyDao dao = DbUtils.getDatabase().getCurrencyDao();

        List<ResponseValuteCursOnDate> currencies = response.getBody()
                .getCursOnDateXMLResponse()
                .getGetCursOnDateXMLResult()
                .getValuteData()
                .getValuteCurses();

        for (int i = 0, size = currencies.size(); i < size; i++) {
            ResponseValuteCursOnDate currentCurs = currencies.get(i);
            CurrencyOnDate currencyOnDate = new CurrencyOnDate(
                    i,
                    currentCurs.valName,
                    currentCurs.nom,
                    currentCurs.curs,
                    currentCurs.code,
                    currentCurs.chCode,
                    formattedDate
            );
            dao.insertCurrencyOnDate(currencyOnDate);
        }
    }

    @Nullable
    private static ResponseGetCursOnDateXMLEnvelope getCurrencyFromDbOrNull(LocalDate date, Throwable throwable) {
        if (hasNetworkException(throwable)) {
            CurrencyDao dao = DbUtils.getDatabase().getCurrencyDao();
            List<CurrencyOnDate> currencies = dao.getCurrenciesOnDate(getInputDateTimeString(date));

            ResponseGetCursOnDateXMLEnvelope respEnvelope = new ResponseGetCursOnDateXMLEnvelope();
            ResponseGetCursOnDateXMLBody respBody = new ResponseGetCursOnDateXMLBody();
            ResponseGetCursOnDateXMLResponse respResponse = new ResponseGetCursOnDateXMLResponse();
            ResponseGetCursOnDateXMLResult respResult = new ResponseGetCursOnDateXMLResult();
            ResponseValuteData respValuteData = new ResponseValuteData();

            List<ResponseValuteCursOnDate> respValuteCursOnDateList = new ArrayList<>();

            for (CurrencyOnDate currencyDb : currencies) {
                ResponseValuteCursOnDate value = new ResponseValuteCursOnDate(
                        currencyDb.getValName(),
                        currencyDb.getNom(),
                        currencyDb.getCurs(),
                        currencyDb.getCode(),
                        currencyDb.getChCode()
                );
                respValuteCursOnDateList.add(value);
            }

            String internalFormattedDate = getFormattedDateString(date, INTERNAL_DATE_PATTERN);
            respValuteData.setOnDate(internalFormattedDate);

            respValuteData.setValuteCurses(respValuteCursOnDateList);
            respResult.setValuteData(respValuteData);
            respResponse.setGetCursOnDateXMLResult(respResult);
            respBody.setCursOnDateXMLResponse(respResponse);
            respEnvelope.setBody(respBody);

            return respEnvelope;
        } else {
            return null;
        }
    }

    @NonNull
    private static CurrencyInRub getCurrencyInRub(String currencyName, ResponseGetCursOnDateXMLEnvelope response) {
        ResponseValuteData responseValuteData = response.getBody()
                .getCursOnDateXMLResponse()
                .getGetCursOnDateXMLResult()
                .getValuteData();

        List<ResponseValuteCursOnDate> currencies = responseValuteData.getValuteCurses();
        String onDateStr = responseValuteData.getOnDate();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(INTERNAL_DATE_PATTERN); //"20200901"
        LocalDate localDate = LocalDate.parse(onDateStr, outputFormatter);

        float currencyValueInRub = 0;

        if (currencies != null) {
            for (ResponseValuteCursOnDate currency : currencies) {
                if (currency.chCode.equals(currencyName)) {
                    currencyValueInRub = parseFloat(currency.curs);
                    break;
                }
            }
        }

        return new CurrencyInRub(currencyName, localDate, currencyValueInRub);
    }

    private static boolean hasNetworkException(Throwable throwable) {
        return HttpUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass());
    }
}
