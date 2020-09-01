package com.andysklyarov.finnotify.framework.soap;

import com.andysklyarov.finnotify.BuildConfig;
import com.andysklyarov.finnotify.framework.soap.SoapCbrApi;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public final class HttpUtils {

    public static final List<Class<?>> NETWORK_EXCEPTIONS = Arrays.asList(
            UnknownHostException.class,
            SocketTimeoutException.class,
            ConnectException.class
    );

    private static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    private static OkHttpClient okHttpClient = null;
    private static Retrofit retrofit = null;
    private static SoapCbrApi api = null;

    public static OkHttpClient getBasicClient(boolean newInstance) {

        if (newInstance || okHttpClient == null) {

            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(httpLoggingInterceptor) // todo clear for realise
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }

    public static Retrofit getRetrofit() {

        if (okHttpClient == null)
            getBasicClient(true);

        if (retrofit == null) {
            Format format = new Format(XML_VERSION_1_0_ENCODING_UTF_8);
            Serializer serializer = new Persister(format);

            retrofit = new Retrofit.Builder() // todo check the sequence
                    .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                    .baseUrl(BuildConfig.CBR_WEBSERV_URL + "/")
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public static SoapCbrApi getApiService() {
        if (api == null)
            api = getRetrofit().create(SoapCbrApi.class);

        return api;
    }
}
