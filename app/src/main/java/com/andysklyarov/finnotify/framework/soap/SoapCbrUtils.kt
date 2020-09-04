package com.andysklyarov.finnotify.framework.soap

import com.andysklyarov.finnotify.domain.CurrencyInRub
import com.andysklyarov.finnotify.domain.CurrencyName
import com.andysklyarov.finnotify.framework.database.CurrencyDao
import com.andysklyarov.finnotify.framework.database.CurrencyDatabase
import com.andysklyarov.finnotify.framework.database.CurrencyOnDate
import com.andysklyarov.finnotify.framework.database.LastDate
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.*
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.*
import io.reactivex.Single
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object SoapCbrUtils {
    private const val INPUT_DATE_TIME_PATTERN = "yyyy-MM-dd"
    private const val TIME_PATTERN = "T00:00:00"
    private const val OUTPUT_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
    private const val INTERNAL_DATE_PATTERN = "yyyyMMdd"

    fun getLastServerDate(): Single<LocalDate> {
        val data = RequestLatestDateTimeData()
        val body = RequestLatestDateTimeBody(data)
        val envelope = RequestLatestDateTimeEnvelope(body)

        return HttpUtils.soapApi.getLatestDateTime(envelope)
            .doOnSuccess { response -> writeLastDateToDb(response) }
            .onErrorReturn { throwable -> getLastDateFromDb(throwable) }
            .map { response ->
                val dateTime = response.body?.latestDateTimeDataResponse?.dateTime
                val outputFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_TIME_PATTERN)
                LocalDate.parse(dateTime, outputFormatter)
            }
    }

    fun getCurrencyOnDate(currencyCode: String, date: LocalDate): Single<CurrencyInRub> {
        val formattedDate = getInputDateTimeString(date)
        val data = RequestGetCursOnDateXMLData(formattedDate)
        val body = RequestGetCursOnDateXMLBody(data)
        val envelope = RequestGetCursOnDateXMLEnvelope(body)

        return HttpUtils.soapApi
            .getCursOnDateXML(envelope)
            .doOnSuccess { response -> writeCurrencyToDb(formattedDate, response) }
            .onErrorReturn { throwable -> getCurrencyFromDbOrNull(date, throwable) }
            .map { response -> getCurrencyInRub(currencyCode, response) }
    }

    private fun writeLastDateToDb(response: ResponseLatestDateTimeEnvelope) {
        val dao: CurrencyDao = CurrencyDatabase.getDatabase().getCurrencyDao()
        val dateTime = response.body?.latestDateTimeDataResponse?.dateTime
        dao.insertLastDate(LastDate(0, dateTime!!))
    }

    private fun getLastDateFromDb(throwable: Throwable): ResponseLatestDateTimeEnvelope? {
        return if (hasNetworkException(throwable)) {
            val dao: CurrencyDao = CurrencyDatabase.getDatabase().getCurrencyDao()

            val date = dao.getLastDate().date
            val data = ResponseLatestDateTimeData(date)
            val body = ResponseLatestDateTimeBody(data)
            val response = ResponseLatestDateTimeEnvelope(body)

            response
        } else {
            null
        }
    }

    private fun getInputDateTimeString(date: LocalDate): String {
        return getFormattedDateString(date, INPUT_DATE_TIME_PATTERN) + TIME_PATTERN
    }

    private fun getFormattedDateString(date: LocalDate, format: String): String {
        return DateTimeFormatter.ofPattern(format).format(date)
    }

    private fun writeCurrencyToDb(
        formattedDate: String,
        response: ResponseGetCursOnDateXMLEnvelope
    ) {
        val dao: CurrencyDao = CurrencyDatabase.getDatabase().getCurrencyDao()
        val currencies = response.body
            ?.cursOnDateXMLResponse
            ?.getCursOnDateXMLResult
            ?.valuteData
            ?.valuteCurses

        for (i in currencies!!.indices) {
            val (valName, nom, curs, code, chCode) = currencies[i]
            val currencyOnDate = CurrencyOnDate(
                i.toLong(), valName, nom, curs, code, chCode, formattedDate
            )
            dao.insertCurrencyOnDate(currencyOnDate)
        }
    }

    private fun getCurrencyFromDbOrNull(
        date: LocalDate,
        throwable: Throwable
    ): ResponseGetCursOnDateXMLEnvelope? {
        return if (hasNetworkException(throwable)) {
            val dao: CurrencyDao = CurrencyDatabase.getDatabase().getCurrencyDao()
            val currencies = dao.getCurrenciesOnDate(getInputDateTimeString(date))

            val respValuteCursOnDateList = ArrayList<ResponseValuteCursOnDate>()

            for (currencyDb in currencies) {
                val value = ResponseValuteCursOnDate(
                    currencyDb.valName,
                    currencyDb.nom,
                    currencyDb.curs,
                    currencyDb.code,
                    currencyDb.chCode
                )
                respValuteCursOnDateList.add(value);
            }

            val internalFormattedDate = getFormattedDateString(date, INTERNAL_DATE_PATTERN)

            val respValuteData = ResponseValuteData(respValuteCursOnDateList, internalFormattedDate)
            val respResult = ResponseGetCursOnDateXMLResult(respValuteData)
            val respResponse = ResponseGetCursOnDateXMLResponse(respResult)
            val respBody = ResponseGetCursOnDateXMLBody(respResponse)
            val respEnvelope = ResponseGetCursOnDateXMLEnvelope(respBody)

            respEnvelope
        } else {
            null
        }
    }

    private fun getCurrencyInRub(
        currencyCode: String,
        response: ResponseGetCursOnDateXMLEnvelope
    ): CurrencyInRub {

        val responseValuteData =
            response.body?.cursOnDateXMLResponse?.getCursOnDateXMLResult?.valuteData

        val currencies = responseValuteData?.valuteCurses
        val onDateStr = responseValuteData?.onDate

        val outputFormatter = DateTimeFormatter.ofPattern(INTERNAL_DATE_PATTERN); //"20200901"
        val localDate = LocalDate.parse(onDateStr, outputFormatter);


        var currencyValueInRub = 0.0f;
        var denomination = 0;
        var currencyName = CurrencyName("Empty", "Empty");

        for (currency in currencies!!) {
            if (currency.chCode == currencyCode) {
                currencyValueInRub = currency.curs.toFloat()
                denomination = currency.nom.toInt()
                currencyName = CurrencyName(currency.valName.trim(), currency.chCode)
            }
        }

        return CurrencyInRub(currencyName, localDate, denomination, currencyValueInRub)
    }

    private fun hasNetworkException(throwable: Throwable): Boolean {
        return HttpUtils.NETWORK_EXCEPTIONS.contains(throwable.javaClass)
    }

}