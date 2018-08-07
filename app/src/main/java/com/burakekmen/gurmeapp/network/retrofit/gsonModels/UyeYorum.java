package com.burakekmen.gurmeapp.network.retrofit.gsonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/****************************
 * Created by Burak EKMEN   |
 * 27.10.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class UyeYorum {

    @SerializedName("yorumID")
    @Expose
    private Integer yorumID;
    @SerializedName("kullaniciAdi")
    @Expose
    private String kullaniciAdi;
    @SerializedName("yorum")
    @Expose
    private String yorum;
    @SerializedName("puan")
    @Expose
    private String puan;
    @SerializedName("tarihi")
    @Expose
    private String tarihi;

    public Integer getYorumID() {
        return yorumID;
    }

    public void setYorumID(Integer yorumID) {
        this.yorumID = yorumID;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }

    public String getPuan() {
        return puan;
    }

    public void setPuan(String puan) {
        this.puan = puan;
    }

    public String getTarihi() {
        return tarihi;
    }

    public void setTarihi(String tarihi) {
        this.tarihi = tarihi;
    }
}
