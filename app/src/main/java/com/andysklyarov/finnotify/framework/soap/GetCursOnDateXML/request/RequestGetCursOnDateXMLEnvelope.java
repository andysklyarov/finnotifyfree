package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;


@Root(name = "soap:Envelope")
@NamespaceList({
        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
        @Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope")
})
public class RequestGetCursOnDateXMLEnvelope {
    @Element(name = "Body")
    @Namespace(reference="http://www.w3.org/2003/05/soap-envelope", prefix="soap")
    private RequestGetCursOnDateXMLBody body;

    public RequestGetCursOnDateXMLBody getBody() {
        return body;
    }

    public void setBody(RequestGetCursOnDateXMLBody body) {
        this.body = body;
    }
}