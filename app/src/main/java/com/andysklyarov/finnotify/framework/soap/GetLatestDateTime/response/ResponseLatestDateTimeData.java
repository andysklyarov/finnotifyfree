package com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetLatestDateTimeResponse")
@Namespace(reference = "http://web.cbr.ru/")
public class ResponseLatestDateTimeData {
    @Element(name = "GetLatestDateTimeResult")
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}