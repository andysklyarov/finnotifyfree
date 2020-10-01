package com.andysklyarov.finnotifyfree.di

import com.andysklyarov.data.BuildConfig
import com.andysklyarov.data.network.SoapCbrApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {
    companion object {
        private const val XML_VERSION_1_0_ENCODING_UTF_8 =
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
        private const val CONNECT_TIMEOUT_MILLISECONDS: Long = 2000
        private const val READ_TIMEOUT_MILLISECONDS: Long = 2000
    }

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()

        if (!BuildConfig.BUILD_TYPE.contains("release")) {
            val httpLoggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(httpLoggingInterceptor)
        }
        builder.connectTimeout(CONNECT_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideSimpleXmlSerializer(): Serializer {
        return Persister(Format(XML_VERSION_1_0_ENCODING_UTF_8))
    }

    @Provides
    @Singleton
    fun getRetrofit(serializer: Serializer, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
            .baseUrl(BuildConfig.CBR_WEBSERV_URL + "/")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSoapCbrApi(retrofit: Retrofit): SoapCbrApi {
        return retrofit.create(SoapCbrApi::class.java)
    }
}