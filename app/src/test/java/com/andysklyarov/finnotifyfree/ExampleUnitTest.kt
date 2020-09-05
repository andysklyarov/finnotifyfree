package com.andysklyarov.   finnotifyfree

import com.andysklyarov.finnotifyfree.framework.soap.GetLatestDateTime.ResponseLatestDateTimeBody
import com.andysklyarov.finnotifyfree.framework.soap.GetLatestDateTime.ResponseLatestDateTimeData
import com.andysklyarov.finnotifyfree.framework.soap.GetLatestDateTime.ResponseLatestDateTimeEnvelope
import org.junit.Test

import org.junit.Assert.*
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.ByteArrayOutputStream

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun serializer_isCorrect() {

        val respData = ResponseLatestDateTimeData("2020-09-04T00:00:00")
        val respBody = ResponseLatestDateTimeBody(respData)
        val respEnvelope = ResponseLatestDateTimeEnvelope(respBody)

        val serializer : Serializer = Persister()
        val outputStream = ByteArrayOutputStream()
        serializer.write(respEnvelope, outputStream)

        val serializer2 : Serializer = Persister()
        val resString = outputStream.toString()
        val respEnv = serializer2.read(ResponseLatestDateTimeEnvelope::class.java, resString)

        val respEnvData = respEnv.body?.latestDateTimeDataResponse?.dateTime
        val respEnvelopeData = respEnvelope.body?.latestDateTimeDataResponse?.dateTime

        assertEquals(respEnvelopeData, respEnvData)
    }
}