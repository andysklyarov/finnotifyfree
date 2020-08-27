package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "soap:Body")
public class RequestGetCursOnDateXMLBody {
    @Element(name = "GetCursOnDateXML")
    @Namespace(reference = "http://web.cbr.ru/")
    private RequestGetCursOnDateXMLData getCursOnDateXML;

    public RequestGetCursOnDateXMLData getGetCursOnDateXML() {
        return getCursOnDateXML;
    }

    public void setGetCursOnDateXML(RequestGetCursOnDateXMLData getCursOnDateXML) {
        this.getCursOnDateXML = getCursOnDateXML;
    }
}