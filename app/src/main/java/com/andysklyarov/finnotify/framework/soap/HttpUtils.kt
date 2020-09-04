package com.andysklyarov.finnotify.framework.soap

import com.andysklyarov.finnotify.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

object HttpUtils {
    val NETWORK_EXCEPTIONS: List<Class<*>> = listOf(
        UnknownHostException::class.java,
        SocketTimeoutException::class.java,
        ConnectException::class.java)

    val soapApi: SoapCbrApi = getRetrofit()!!.create<SoapCbrApi>(SoapCbrApi::class.java)

    private const val XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"

    private var okHttpClient: OkHttpClient? = null
    private var retrofit: Retrofit? = null

    private fun getBasicClient(newInstance: Boolean): OkHttpClient? {
        if (newInstance || okHttpClient == null) {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor) // todo clear for realise
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build()
        }
        return okHttpClient
    }

    private fun getRetrofit(): Retrofit? {
        if (okHttpClient == null) getBasicClient(true)
        if (retrofit == null) {
            val format = Format(XML_VERSION_1_0_ENCODING_UTF_8)
            val serializer: Serializer = Persister(format)
            retrofit = Retrofit.Builder() // todo check the sequence
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer)) // todo check the data classes
                .baseUrl(BuildConfig.CBR_WEBSERV_URL.toString() + "/")
                .client(this.okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return retrofit
    }
}