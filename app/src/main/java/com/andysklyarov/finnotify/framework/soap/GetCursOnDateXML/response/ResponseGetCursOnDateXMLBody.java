package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "soap:Body")
public class ResponseGetCursOnDateXMLBody {
    @Element(name = "GetCursOnDateXMLResponse")
    @Namespace(reference="http://web.cbr.ru/")
    private ResponseGetCursOnDateXMLResponse cursOnDateXMLResponse;

    public ResponseGetCursOnDateXMLResponse getCursOnDateXMLResponse() {
        return cursOnDateXMLResponse;
    }

    public void setCursOnDateXMLResponse(ResponseGetCursOnDateXMLResponse cursOnDateXMLResponse) {
        this.cursOnDateXMLResponse = cursOnDateXMLResponse;
    }
}