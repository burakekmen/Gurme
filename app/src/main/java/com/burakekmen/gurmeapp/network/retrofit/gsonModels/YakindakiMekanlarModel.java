package com.burakekmen.gurmeapp.network.retrofit.gsonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/****************************
 * Created by Burak EKMEN   |
 * 27.10.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class YakindakiMekanlarModel {

    @SerializedName("mekanID")
    @Expose
    private Integer mekanID;
    @SerializedName("mekanAd")
    @Expose
    private String mekanAd;
    @SerializedName("lati")
    @Expose
    private String lati;
    @SerializedName("longlati")
    @Expose
    private String longlati;

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
}
