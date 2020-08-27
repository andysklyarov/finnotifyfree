package com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.request;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "soap:Body")
public class RequestLatestDateTimeBody {
    @Element(name = "GetLatestDateTime")
    private RequestLatestDateTimeData latestDateTimeData ;

    public RequestLatestDateTimeData getLatestDateTimeData() {
        return latestDateTimeData;
    }

    public void setLatestDateTimeData(RequestLatestDateTimeData latestDateTimeData) {
        this.latestDateTimeData = latestDateTimeData;
    }
}