package com.andysklyarov.data.network


import com.andysklyarov.data.BuildConfig
import com.andysklyarov.data.network.GetCursOnDateXML.RequestGetCursOnDateXMLEnvelope
import com.andysklyarov.data.network.GetCursOnDateXML.ResponseGetCursOnDateXMLEnvelope
import com.andysklyarov.data.network.GetLatestDateTime.RequestLatestDateTimeEnvelope
import com.andysklyarov.data.network.GetLatestDateTime.ResponseLatestDateTimeEnvelope
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