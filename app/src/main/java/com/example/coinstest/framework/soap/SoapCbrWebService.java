package com.example.coinstest.framework.soap;

import android.util.Log;

import com.example.coinstest.domain.CurrencyInRub;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutionException;

import static com.example.coinstest.framework.soap.SoapCbrResources.CBR_GET_CURS_ON_DATE_XML;
import static com.example.coinstest.framework.soap.SoapCbrResources.CBR_GET_LATEST_DATE_TIME;

// TODO переделать под Clean Code
public final class SoapCbrWebService {
    private final String LOG_TAG = "CoinsTest";
    private SoapCbrAsyncTask soapCbrAsyncTask;

    public CurrencyInRub getCurrency(String currencyName, String date) {

        String dataTimeInXml = "dateTime";
        StringBuilder strXml = new StringBuilder(CBR_GET_CURS_ON_DATE_XML);
        int posDataTimeInXml = strXml.lastIndexOf(dataTimeInXml);
        strXml.replace(posDataTimeInXml, posDataTimeInXml + dataTimeInXml.length(), date);

        soapCbrAsyncTask = new SoapCbrAsyncTask(strXml.toString(), 2000);
        String reqXML = null;
        try {
            reqXML = soapCbrAsyncTask.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        float currencyValue = GetCurs(reqXML, currencyName);
        return new CurrencyInRub(date, currencyName, currencyValue);
    }

    public String getLastServerDate() {

        soapCbrAsyncTask = new SoapCbrAsyncTask(CBR_GET_LATEST_DATE_TIME, 2000);
        String reqXML = null;
        try {
            reqXML = soapCbrAsyncTask.execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return GetDataTime(reqXML); //"2020-04-30T00:00:00"
    }

    /**
     * Currency = "USD" or "EUR"
     */
    private float GetCurs(String reqXML, String Currency) {
        String result = "";
        String tagName = "";
        boolean isNext = true;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(reqXML));

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && isNext) {
                switch (xpp.getEventType()) {
                    // начало документа
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    // начало тэга
                    case XmlPullParser.START_TAG:
                        tagName = xpp.getName();
                        break;

                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        if (tagName.equals("VchCode") && xpp.getText().equals(Currency))
                            isNext = false;
                        else if (tagName.equals("Vcurs"))
                            result = xpp.getText();

                        tagName = "";
                        break;

                    // конец тэга
                    case XmlPullParser.END_TAG:
                        break;

                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            Log.e(LOG_TAG, "Big ASS");
        }

        return Float.parseFloat(result);
    }

    private String GetDataTime(String reqXML) {
        String result = "";
        String tagName = "";
        boolean isNext = true;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(reqXML));

            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && isNext) {
                switch (xpp.getEventType()) {
                    // начало тэга
                    case XmlPullParser.START_TAG:
                        tagName = xpp.getName();
                        break;

                    // содержимое тэга
                    case XmlPullParser.TEXT:
                        if (tagName.equals("GetLatestDateTimeResult")) {
                            result = xpp.getText();
                            isNext = false;
                        }
                        break;

                    default:
                        break;
                }
                // следующий элемент
                xpp.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            Log.e(LOG_TAG, "Big ASS");
        }

        return result;
    }
}
