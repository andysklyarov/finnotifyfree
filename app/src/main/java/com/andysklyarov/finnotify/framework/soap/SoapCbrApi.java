package com.andysklyarov.finnotify.framework.soap;

import com.andysklyarov.finnotify.BuildConfig;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request.RequestGetCursOnDateXMLEnvelope;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseGetCursOnDateXMLEnvelope;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.request.RequestLatestDateTimeEnvelope;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.response.ResponseLatestDateTimeEnvelope;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SoapCbrApi {

    @Headers({
            "Host: " + BuildConfig.CBR_HOST,
            "Content-Type: "+ BuildConfig.CBR_CONTENT_TYPE
    })
    @POST(BuildConfig.CBR_WEBSERV_URL)
    Single<ResponseLatestDateTimeEnvelope> getLatestDateTime(@Body RequestLatestDateTimeEnvelope body);


    @Headers({
            "Host: " + BuildConfig.CBR_HOST,
            "Content-Type: "+ BuildConfig.CBR_CONTENT_TYPE
    })
    @POST(BuildConfig.CBR_WEBSERV_URL)
    Single<ResponseGetCursOnDateXMLEnvelope> getCursOnDateXML(@Body RequestGetCursOnDateXMLEnvelope body);
}