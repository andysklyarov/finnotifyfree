package com.andysklyarov.finnotify.framework.soap;

import android.os.AsyncTask;
import android.util.Log;

import com.andysklyarov.finnotify.BuildConfig;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

final class CallWebServiceAsyncTask extends AsyncTask<Void, Void, String> {

    private int httpTimeout;
    private String xmlRequest;

    CallWebServiceAsyncTask(String xmlRequest, int httpTimeout) {
        super();
        this.xmlRequest = xmlRequest;
        this.httpTimeout = httpTimeout;
    }

    private static String CallWebServiceHttpURLConnection(String envelope, int httpTimeout) {

        HttpURLConnection urlConnection = null;
        String strResult = "No data";

        try {
            //Set URL connection
            urlConnection = (HttpURLConnection) new URL(BuildConfig.CBR_WEBSERV_URL).openConnection();
            urlConnection.setReadTimeout(httpTimeout);
            urlConnection.setConnectTimeout(httpTimeout);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Host", BuildConfig.CBR_HOST);
            urlConnection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(envelope.getBytes().length));

            //Send POST data
            OutputStream os = urlConnection.getOutputStream();
            byte[] data = envelope.getBytes(StandardCharsets.UTF_8);
            os.write(data);

            //Get response
            urlConnection.connect();

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                throw new RuntimeException(urlConnection.getResponseMessage());
            else {
                BufferedInputStream bufIn = new BufferedInputStream(urlConnection.getInputStream());
                ByteArrayOutputStream bufOut = new ByteArrayOutputStream();

                int bytesRead = bufIn.read();
                while (bytesRead != -1) {
                    bufOut.write((byte) bytesRead); // некруто
                    bytesRead = bufIn.read();
                }
                strResult = bufOut.toString();
            }
        } catch (Exception ex) {
            Log.e("CoinsTest", ex.getMessage(), ex);
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return strResult;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        return CallWebServiceHttpURLConnection(xmlRequest, httpTimeout);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
