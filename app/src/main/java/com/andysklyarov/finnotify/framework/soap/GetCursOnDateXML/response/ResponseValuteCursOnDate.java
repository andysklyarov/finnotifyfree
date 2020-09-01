package com.andysklyarov.finnotify.framework.soap.GetCursOnDateXML.response;

import androidx.room.Ignore;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ValuteCursOnDate")
public class ResponseValuteCursOnDate {
    @Element(name = "Vname")
    public String valName;

    @Element(name = "Vnom")
    public String nom;

    @Element(name = "Vcurs")
    public String curs;

    @Element(name = "Vcode")
    public String code;

    @Element(name = "VchCode")
    public String chCode;

    public ResponseValuteCursOnDate() {

    }

    @Ignore
    public ResponseValuteCursOnDate(String valName, String nom, String curs, String code, String chCode) {
        this.valName = valName;
        this.nom = nom;
        this.curs = curs;
        this.code = code;
        this.chCode = chCode;
    }
}
