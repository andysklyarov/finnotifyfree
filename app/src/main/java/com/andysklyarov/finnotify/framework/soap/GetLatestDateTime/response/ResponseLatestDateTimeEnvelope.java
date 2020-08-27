package com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.response;

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
public class ResponseLatestDateTimeEnvelope {
    @Element(name = "Body")
    @Namespace(reference="http://www.w3.org/2003/05/soap-envelope", prefix="soap")
    private ResponseLatestDateTimeBody body;

    public ResponseLatestDateTimeBody getBody() {
        return body;
    }

    public void setBody(ResponseLatestDateTimeBody body) {
        this.body = body;
    }
}