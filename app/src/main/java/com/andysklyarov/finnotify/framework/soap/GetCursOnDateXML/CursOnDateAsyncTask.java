package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML;

import android.os.AsyncTask;

import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request.RequestGetCursOnDateXMLData;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request.RequestGetCursOnDateXMLBody;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request.RequestGetCursOnDateXMLEnvelope;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseGetCursOnDateXMLEnvelope;
import com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response.ResponseValuteCursOnDate;
import com.andysklyarov.finnotify.framework.soap.SoapUtils;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

//TODO заменить на RJx
public final class CursOnDateAsyncTask extends AsyncTask<Void, Void, List<ResponseValuteCursOnDate>> {

    private volatile String dateTime;
    private List<ResponseValuteCursOnDate> result = null;

    public CursOnDateAsyncTask(String dateTime) {
        super();
        this.dateTime = dateTime;
    }

    private List<ResponseValuteCursOnDate> CallWebServiceRetrofit() {
        RequestGetCursOnDateXMLEnvelope envelope = new RequestGetCursOnDateXMLEnvelope();
        RequestGetCursOnDateXMLBody body = new RequestGetCursOnDateXMLBody();
        RequestGetCursOnDateXMLData data = new RequestGetCursOnDateXMLData();

        data.setDateTime(dateTime);
        body.setGetCursOnDateXML(data);
        envelope.setBody(body);

        Call<ResponseGetCursOnDateXMLEnvelope> call = SoapUtils.getApiService().getCursOnDateXML(envelope);
        Response<ResponseGetCursOnDateXMLEnvelope> response;
        try {
            response = call.execute();

            if (response.isSuccessful()) {
                result = response.body().getBody().getCursOnDateXMLResponse().getGetCursOnDateXMLResult().getValuteData().getValuteCurses();
            } else {
                result = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<ResponseValuteCursOnDate> doInBackground(Void... params) {
        return CallWebServiceRetrofit();
    }

    @Override
    protected void onPostExecute(List<ResponseValuteCursOnDate> result) {
        super.onPostExecute(result);
    }
}
