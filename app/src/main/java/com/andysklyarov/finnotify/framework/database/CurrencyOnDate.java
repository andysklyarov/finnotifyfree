package com.andysklyarov.finnotify.framework.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class CurrencyOnDate {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "Vname")
    private String valName;

    @ColumnInfo(name = "Vnom")
    private String nom;

    @ColumnInfo(name = "Vcurs")
    private String curs;

    @ColumnInfo(name = "Vcode")
    private String code;

    @ColumnInfo(name = "VchCode")
    private String chCode;

    @ColumnInfo(name = "DateTime")
    private String date;

    public CurrencyOnDate(long id, String valName, String nom, String curs, String code, String chCode, String date) {
        this.id = id;
        this.valName = valName;
        this.nom = nom;
        this.curs = curs;
        this.code = code;
        this.chCode = chCode;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValName() {
        return valName;
    }

    public void setValName(String valName) {
        this.valName = valName;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCurs() {
        return curs;
    }

    public void setCurs(String curs) {
        this.curs = curs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getChCode() {
        return chCode;
    }

    public void setChCode(String chCode) {
        this.chCode = chCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}