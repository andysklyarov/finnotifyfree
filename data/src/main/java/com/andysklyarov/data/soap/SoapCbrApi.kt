package com.andysklyarov.data.soap


import com.andysklyarov.data.BuildConfig
import com.andysklyarov.data.soap.GetCursOnDateXML.RequestGetCursOnDateXMLEnvelope
import com.andysklyarov.data.soap.GetCursOnDateXML.ResponseGetCursOnDateXMLEnvelope
import com.andysklyarov.data.soap.GetLatestDateTime.RequestLatestDateTimeEnvelope
import com.andysklyarov.data.soap.GetLatestDateTime.ResponseLatestDateTimeEnvelope
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SoapCbrApi {

    @Headers(
        "Host: " + BuildConfig.CBR_HOST,
        "Content-Type: " + BuildConfig.CBR_CONTENT_TYPE
    )
    @POST(BuildConfig.CBR_WEBSERV_URL)
    fun getLatestDateTime(@Body body: RequestLatestDateTimeEnvelope): Single<ResponseLatestDateTimeEnvelope>


    @Headers(
        "Host: " + BuildConfig.CBR_HOST,
        "Content-Type: " + BuildConfig.CBR_CONTENT_TYPE
    )
    @POST(BuildConfig.CBR_WEBSERV_URL)
    fun getCursOnDateXML(@Body body: RequestGetCursOnDateXMLEnvelope): Single<ResponseGetCursOnDateXMLEnvelope>
}