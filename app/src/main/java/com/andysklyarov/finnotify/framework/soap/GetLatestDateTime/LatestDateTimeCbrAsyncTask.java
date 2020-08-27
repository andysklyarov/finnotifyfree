package com.andysklyarov.finnotify.framework.soap.GetLatestDateTime;

import android.os.AsyncTask;

import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.request.RequestLatestDateTimeBody;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.request.RequestLatestDateTimeData;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.request.RequestLatestDateTimeEnvelope;
import com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.response.ResponseLatestDateTimeEnvelope;
import com.andysklyarov.finnotify.framework.soap.SoapUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

//TODO заменить на RJx
public final class LatestDateTimeCbrAsyncTask extends AsyncTask<Void, Void, String> {

    private volatile String dateTime;

    public LatestDateTimeCbrAsyncTask() {
        super();
    }

    private String CallWebServiceRetrofit() {
        RequestLatestDateTimeEnvelope envelope = new RequestLatestDateTimeEnvelope();
        RequestLatestDateTimeBody body = new RequestLatestDateTimeBody();
        RequestLatestDateTimeData data = new RequestLatestDateTimeData();

        body.setLatestDateTimeData(data);
        envelope.setBody(body);

        Call<ResponseLatestDateTimeEnvelope> call = SoapUtils.getApiService().getLatestDateTime(envelope);

        Response<ResponseLatestDateTimeEnvelope> response;
        try {
            response = call.execute();

            if (response.isSuccessful()) {
                dateTime = response.body().getBody().getLatestDateTimeData().getDateTime();
            } else {
                dateTime = "1111-11-11T00:00:00";
                //todo make error handling
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
//        call.enqueue(new retrofit2.Callback<ResponseLatestDateTimeEnvelope>() {
//            @Override
//            public void onResponse(Call<ResponseLatestDateTimeEnvelope> call, Response<ResponseLatestDateTimeEnvelope> response) {
//                if (response.isSuccessful()) {
//                    dateTime = response.body().getBody().getLatestDateTimeData().getDateTime();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseLatestDateTimeEnvelope> call, Throwable t) {
//                dateTime="1111-11-11T00:00:00";
//            }
//        });
        return dateTime;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        return CallWebServiceRetrofit();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
