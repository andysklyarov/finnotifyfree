package com.andysklyarov.finnotifyfree

import com.andysklyarov.data.soap.GetLatestDateTime.ResponseLatestDateTimeBody
import com.andysklyarov.data.soap.GetLatestDateTime.ResponseLatestDateTimeData
import com.andysklyarov.data.soap.GetLatestDateTime.ResponseLatestDateTimeEnvelope
import dagger.Component
import org.junit.Assert.assertEquals
import org.junit.Test
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

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

        val serializer: Serializer = Persister()
        val outputStream = ByteArrayOutputStream()
        serializer.write(respEnvelope, outputStream)

        val serializer2: Serializer = Persister()
        val resString = outputStream.toString()
        val respEnv = serializer2.read(ResponseLatestDateTimeEnvelope::class.java, resString)

        val respEnvData = respEnv.body?.latestDateTimeDataResponse?.dateTime
        val respEnvelopeData = respEnvelope.body?.latestDateTimeDataResponse?.dateTime

        assertEquals(respData, respEnvData)
    }
}