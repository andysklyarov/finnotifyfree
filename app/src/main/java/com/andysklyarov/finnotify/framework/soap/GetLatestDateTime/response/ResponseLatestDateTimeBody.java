package com.andysklyarov.finnotify.framework.soap.GetLatestDateTime.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

@Root(name = "soap:Body")
public class ResponseLatestDateTimeBody {
    @Element(name = "GetLatestDateTimeResponse")
    @Namespace(reference="http://web.cbr.ru/")
    private ResponseLatestDateTimeData latestDateTimeDataResponse;

    public ResponseLatestDateTimeData getLatestDateTimeData() {
        return latestDateTimeDataResponse;
    }

    public void setLatestDateTimeData(ResponseLatestDateTimeData latestDateTimeData) {
        this.latestDateTimeDataResponse = latestDateTimeData;
    }
}