package com.example.coinstest.framework.soap;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static com.example.coinstest.framework.soap.SoapCbrResources.CBR;
import static com.example.coinstest.framework.soap.SoapCbrResources.CBR_URL;

//TODO заменить на RJx
final class SoapCbrAsyncTask extends AsyncTask<Void, Void, String> {

    private int httpTimeout;
    private String xmlRequest;

    SoapCbrAsyncTask(String xmlRequest, int httpTimeout) {
        super();
        this.xmlRequest = xmlRequest;
        this.httpTimeout = httpTimeout;
    }

    private static String CallWebService(String envelope, int httpTimeout) {

        HttpURLConnection urlConnection = null;
        String strResult = "No data";

        try {
            //Set URL connection
            urlConnection = (HttpURLConnection) new URL(CBR_URL).openConnection();

            urlConnection.setReadTimeout(httpTimeout);
            urlConnection.setConnectTimeout(httpTimeout);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Host", CBR);
            urlConnection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(envelope.getBytes().length));

            //Send POST data
            OutputStream os = urlConnection.getOutputStream();
            byte[] data = new byte[0];
            data = envelope.getBytes(StandardCharsets.UTF_8);
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
        return CallWebService(xmlRequest, httpTimeout);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
