package com.burakekmen.gurmeapp.network.retrofit.gsonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/****************************
 * Created by Burak EKMEN   |
 * 27.10.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class UyeBilgileri {

    @SerializedName("uyeID")
    @Expose
    private Integer uyeID;
    @SerializedName("ad")
    @Expose
    private String ad;
    @SerializedName("soyad")
    @Expose
    private String soyad;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("sifre")
    @Expose
    private String sifre;

    public Integer getUyeID() {
        return uyeID;
    }

    public void setUyeID(Integer uyeID) {
        this.uyeID = uyeID;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }
}
