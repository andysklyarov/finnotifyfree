package com.example.coinstest.framework.soap;

final class SoapCbrResources {
    final static String CBR_GET_LATEST_DATE_TIME = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance/\" " +
            "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"> " +
            "<soap12:Body> <GetLatestDateTime xmlns=\"http://web.cbr.ru/\" /> </soap12:Body> " +
            "</soap12:Envelope>";

    final static String CBR_GET_CURS_ON_DATE_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?> " +
            "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
            "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
            "xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\"> " +
            "<soap12:Body> <GetCursOnDateXML xmlns=\"http://web.cbr.ru/\"> <On_date>dateTime</On_date> " +
            "</GetCursOnDateXML> </soap12:Body> </soap12:Envelope>";

    final static String CBR_URL = "https://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx";
    final static String CBR = "www.cbr.ru";
}