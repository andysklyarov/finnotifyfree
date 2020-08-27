package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "GetCursOnDateXMLResult")
public class ResponseGetCursOnDateXMLResult {
    @Element(name = "ValuteData")
    @Namespace(reference = "")
    private ResponseValuteData valuteData;

    public ResponseValuteData getValuteData() {
        return valuteData;
    }

    public void setValuteData(ResponseValuteData valuteData) {
        this.valuteData = valuteData;
    }
}