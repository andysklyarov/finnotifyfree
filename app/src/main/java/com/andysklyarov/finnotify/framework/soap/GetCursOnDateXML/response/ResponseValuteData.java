package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "ValuteData")
@Namespace(reference = "")
public class ResponseValuteData {
    @ElementList(name = "ValuteCursOnDate", inline = true)
    private List<ResponseValuteCursOnDate> valuteCurses;

    @Attribute(name = "OnDate")
    private String onDate;

    public List<ResponseValuteCursOnDate> getValuteCurses() {
        return valuteCurses;
    }

    public void setValuteCurses(List<ResponseValuteCursOnDate> valuteCurses) {
        this.valuteCurses = valuteCurses;
    }

    public String getOnDate() {
        return onDate;
    }

    public void setOnDate(String onDate) {
        this.onDate = onDate;
    }
}
