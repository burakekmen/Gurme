package com.burakekmen.gurmeapp.network.retrofit.gsonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/****************************
 * Created by Burak EKMEN   |
 * 27.10.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class MekanDetayModel {

    @SerializedName("mekanID")
    @Expose
    private Integer mekanID;
    @SerializedName("mekanAd")
    @Expose
    private String mekanAd;
    @SerializedName("mekanTanitimYazisi")
    @Expose
    private String mekanTanitimYazisi;
    @SerializedName("mekanAdres")
    @Expose
    private String mekanAdres;
    @SerializedName("telefon")
    @Expose
    private String telefon;
    @SerializedName("mekanPuan")
    @Expose
    private String mekanPuan;
    @SerializedName("mekanZiyaret")
    @Expose
    private String mekanZiyaret;
    @SerializedName("lati")
    @Expose
    private String lati;
    @SerializedName("longlati")
    @Expose
    private String longlati;
    @SerializedName("mekanResimsUrl")
    @Expose
    private ArrayList<MekanResimsUrl> mekanResimsUrl = null;
    @SerializedName("yorumlar")
    @Expose
    private ArrayList<UyeYorum> yorumlar = null;
    @SerializedName("favori")
    @Expose
    private Boolean favori;

    public Integer getMekanID() {
        return mekanID;
    }

    public void setMekanID(Integer mekanID) {
        this.mekanID = mekanID;
    }

    public String getMekanAd() {
        return mekanAd;
    }

    public void setMekanAd(String mekanAd) {
        this.mekanAd = mekanAd;
    }

    public String getMekanTanitimYazisi() {
        return mekanTanitimYazisi;
    }

    public void setMekanTanitimYazisi(String mekanTanitimYazisi) {
        this.mekanTanitimYazisi = mekanTanitimYazisi;
    }

    public String getMekanAdres() {
        return mekanAdres;
    }

    public void setMekanAdres(String mekanAdres) {
        this.mekanAdres = mekanAdres;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMekanPuan() {
        return mekanPuan;
    }

    public void setMekanPuan(String mekanPuan) {
        this.mekanPuan = mekanPuan;
    }

    public String getMekanZiyaret() {
        return mekanZiyaret;
    }

    public void setMekanZiyaret(String mekanZiyaret) {
        this.mekanZiyaret = mekanZiyaret;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLonglati() {
        return longlati;
    }

    public void setLonglati(String longlati) {
        this.longlati = longlati;
    }

    public ArrayList<MekanResimsUrl> getMekanResimsUrl() {
        return mekanResimsUrl;
    }

    public void setMekanResimsUrl(ArrayList<MekanResimsUrl> mekanResimsUrl) {
        this.mekanResimsUrl = mekanResimsUrl;
    }

    public ArrayList<UyeYorum> getYorumlar() {
        return yorumlar;
    }

    public void setYorumlar(ArrayList<UyeYorum> yorumlar) {
        this.yorumlar = yorumlar;
    }

    public Boolean getFavori() {
        return favori;
    }

    public void setFavori(Boolean favori) {
        this.favori = favori;
    }
}
