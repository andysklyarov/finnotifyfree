package com.andysklyarov.data.repository

import com.andysklyarov.data.network.GetCursOnDateXML.RequestGetCursOnDateXMLBody
import com.andysklyarov.data.network.GetCursOnDateXML.RequestGetCursOnDateXMLData
import com.andysklyarov.data.network.GetCursOnDateXML.RequestGetCursOnDateXMLEnvelope
import com.andysklyarov.data.network.GetCursOnDateXML.ResponseGetCursOnDateXMLEnvelope
import com.andysklyarov.data.network.GetLatestDateTime.RequestLatestDateTimeBody
import com.andysklyarov.data.network.GetLatestDateTime.RequestLatestDateTimeData
import com.andysklyarov.data.network.GetLatestDateTime.RequestLatestDateTimeEnvelope
import com.andysklyarov.data.network.SoapCbrApi
import com.andysklyarov.domain.model.CurrencyInRub
import com.andysklyarov.domain.repository.CurrencyInRubRepository
import io.reactivex.Single
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CurrencyInRubServerRepository @Inject constructor() : CurrencyInRubRepository {

    @Inject
    lateinit var soapCbrApi: SoapCbrApi

    constructor(soapCbrApi: SoapCbrApi) : this() {
        this.soapCbrApi = soapCbrApi
    }

    override fun getLastDate(): Single<LocalDate> {
        val data = RequestLatestDateTimeData()
        val body = RequestLatestDateTimeBody(data)
        val envelope = RequestLatestDateTimeEnvelope(body)

        return soapCbrApi.getLatestDateTime(envelope)
            .map { response ->
                val dateTime = response.body?.latestDateTimeDataResponse?.dateTime
                val outputFormatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_TIME_PATTERN)
                LocalDate.parse(dateTime, outputFormatter)
            }
    }

    override fun getCurrenciesList(date: LocalDate): Single<List<CurrencyInRub>> {
        return getCursOnDateResponse(date.minusDays(1))
            .flatMap { oldResponse ->
                getCursOnDateResponse(date)
                    .map { newResponse ->
                        getCurrencyInRubWithDiff(newResponse, oldResponse)
                    }
            }
    }

    override fun insertCurrenciesList(currencies: List<CurrencyInRub>, date: LocalDate) {
        //do nothing
    }

    private fun getCursOnDateResponse(date: LocalDate): Single<ResponseGetCursOnDateXMLEnvelope> {
        val formattedDate = getInputDateTimeString(date)
        val data = RequestGetCursOnDateXMLData(formattedDate)
        val body = RequestGetCursOnDateXMLBody(data)
        val envelope = RequestGetCursOnDateXMLEnvelope(body)

        return soapCbrApi.getCursOnDateXML(envelope)
    }

    private fun getCurrencyInRubWithDiff(
        newResponse: ResponseGetCursOnDateXMLEnvelope,
        oldResponse: ResponseGetCursOnDateXMLEnvelope
    ): List<CurrencyInRub> {

        val oldData =
            oldResponse.body?.cursOnDateXMLResponse?.getCursOnDateXMLResult?.valuteData
        val oldCurrencies = oldData?.valuteCurses

        val newData =
            newResponse.body?.cursOnDateXMLResponse?.getCursOnDateXMLResult?.valuteData
        val newCurrencies = newData?.valuteCurses

        val result: ArrayList<CurrencyInRub> = ArrayList()

        if (oldCurrencies != null && newCurrencies != null) {
            val onDateStr = newData.onDate
            val outputFormatter = DateTimeFormatter.ofPattern(INTERNAL_DATE_PATTERN) //"20200901"
            val localDate = LocalDate.parse(onDateStr, outputFormatter)

            for (i in newCurrencies.indices) {
                result.add(
                    CurrencyInRub(
                        newCurrencies[i].valName.trim(),
                        newCurrencies[i].chCode,
                        localDate,
                        newCurrencies[i].nom.toInt(),
                        newCurrencies[i].curs.toFloat(),
                        newCurrencies[i].curs.toFloat() - oldCurrencies[i].curs.toFloat() //NPE
                    )
                )
            }
            return result
        } else {
            result.add(CurrencyInRub("Empty", "EMP", LocalDate.now(), 0, 0f, 0f))
            return result
        }
    }
}