package com.andysklyarov.finnotify.framework.soap;

import com.andysklyarov.finnotify.BuildConfig;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class SoapUtils {
    private static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static CbrApi api;

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

            retrofit = new Retrofit.Builder()
                    .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
                    .baseUrl(BuildConfig.CBR_WEBSERV_URL + "/")
                    .client(okHttpClient)
                    .build();

        }
        return retrofit;
    }

    public static CbrApi getApiService() {
        if (api == null)
            api = getRetrofit().create(CbrApi.class);

        return api;
    }

}
