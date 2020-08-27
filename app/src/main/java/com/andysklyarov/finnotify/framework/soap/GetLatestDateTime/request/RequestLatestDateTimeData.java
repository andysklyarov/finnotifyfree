package com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.request;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "GetLatestDateTime")
public class RequestLatestDateTimeData {
    @Attribute(name = "xmlns")
    private String xmlns = "http://web.cbr.ru/";

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }
}