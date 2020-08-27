package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetCursOnDateXML")
@Namespace(reference = "http://web.cbr.ru/")
public class RequestGetCursOnDateXMLData {
    @Element(name = "On_date")
    private String dateTime = "dateTime";

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}