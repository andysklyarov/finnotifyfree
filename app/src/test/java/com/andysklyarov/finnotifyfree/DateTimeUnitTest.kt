package com.andysklyarov.finnotifyfree

import com.andysklyarov.data.network.GetLatestDateTime.*
import com.andysklyarov.data.network.SoapCbrApi
import com.andysklyarov.data.repository.CurrencyInRubServerRepository
import com.andysklyarov.data.repository.OUTPUT_DATE_TIME_PATTERN
import com.andysklyarov.domain.interactors.CurrencyInteractorsImpl
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.internal.operators.single.SingleJust
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

@RunWith(MockitoJUnitRunner::class)
class DateTimeUnitTest {

    @Mock
    private lateinit var soapCbrApi: SoapCbrApi

    private lateinit var serverRepository: CurrencyInRubServerRepository

    @Before
    fun init() {
        serverRepository = CurrencyInRubServerRepository(soapCbrApi)
    }

    @Test
    fun getLastDateTest() {
        val data = RequestLatestDateTimeData()
        val body = RequestLatestDateTimeBody(data)
        val envelope = RequestLatestDateTimeEnvelope(body)

        val respData = ResponseLatestDateTimeData()
        respData.dateTime = "2020-10-21T00:00:00"
        val respBody = ResponseLatestDateTimeBody(respData)
        val respEnvelope = ResponseLatestDateTimeEnvelope(respBody)

        `when`(soapCbrApi.getLatestDateTime(envelope)).thenReturn(Single.just(respEnvelope))

        val localDate = serverRepository.getLastDate().blockingGet()
        assertEquals(LocalDate.of(2020, 10, 21), localDate)
    }
}