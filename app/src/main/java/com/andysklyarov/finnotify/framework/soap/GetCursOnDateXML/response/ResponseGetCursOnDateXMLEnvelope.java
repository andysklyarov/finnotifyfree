package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Root;

@Root(name = "soap:Envelope")
@NamespaceList({
        @Namespace(prefix = "soap", reference = "http://www.w3.org/2003/05/soap-envelope"),
        @Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance"),
        @Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema")
})
public class ResponseGetCursOnDateXMLEnvelope {
    @Element(name = "Body")
    @Namespace(reference="http://www.w3.org/2003/05/soap-envelope", prefix="soap")
    private ResponseGetCursOnDateXMLBody body;

    public ResponseGetCursOnDateXMLBody getBody() {
        return body;
    }

    public void setBody(ResponseGetCursOnDateXMLBody body) {
        this.body = body;
    }
}