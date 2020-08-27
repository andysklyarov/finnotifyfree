package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetCursOnDateXMLResponse")
@Namespace(reference = "http://web.cbr.ru/")
public class ResponseGetCursOnDateXMLResponse {
    @Element(name = "GetCursOnDateXMLResult")
    private ResponseGetCursOnDateXMLResult getCursOnDateXMLResult;

    public ResponseGetCursOnDateXMLResult getGetCursOnDateXMLResult() {
        return getCursOnDateXMLResult;
    }

    public void setGetCursOnDateXMLResult(ResponseGetCursOnDateXMLResult getCursOnDateXMLResult) {
        this.getCursOnDateXMLResult = getCursOnDateXMLResult;
    }
}