package com.burakekmen.gurmeapp.network.retrofit.gsonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/****************************
 * Created by Burak EKMEN   |
 * 27.10.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class TumYorumlarModel {

    @SerializedName("yorumID")
    @Expose
    private Integer yorumID;
    @SerializedName("mekanID")
    @Expose
    private Integer mekanID;
    @SerializedName("uyeID")
    @Expose
    private Integer uyeID;
    @SerializedName("kullaniciAdi")
    @Expose
    private String kullaniciAdi;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("yorum")
    @Expose
    private String yorum;
    @SerializedName("puanID")
    @Expose
    private Integer puanID;
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

    public Integer getMekanID() {
        return mekanID;
    }

    public void setMekanID(Integer mekanID) {
        this.mekanID = mekanID;
    }

    public Integer getUyeID() {
        return uyeID;
    }

    public void setUyeID(Integer uyeID) {
        this.uyeID = uyeID;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getYorum() {
        return yorum;
    }

    public void setYorum(String yorum) {
        this.yorum = yorum;
    }

    public Integer getPuanID() {
        return puanID;
    }

    public void setPuanID(Integer puanID) {
        this.puanID = puanID;
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
