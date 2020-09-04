package com.andysklyarov.finnotify

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.ResponseLatestDateTimeBody
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.ResponseLatestDateTimeData
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.ResponseLatestDateTimeEnvelope

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import java.io.ByteArrayOutputStream

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
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