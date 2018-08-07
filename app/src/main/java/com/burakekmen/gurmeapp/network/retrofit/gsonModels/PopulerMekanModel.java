package com.burakekmen.gurmeapp.network.retrofit.gsonModels;

/****************************
 * Created by Burak EKMEN   |
 * 27.10.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PopulerMekanModel implements Parcelable{

    @SerializedName("mekanID")
    @Expose
    private Integer mekanID;
    @SerializedName("mekanAd")
    @Expose
    private String mekanAd;
    @SerializedName("kapakResmi")
    @Expose
    private String kapakResmi;
    @SerializedName("puan")
    @Expose
    private String puan;
    @SerializedName("adresi")
    @Expose
    private String adresi;
    @SerializedName("ziyaret")
    @Expose
    private String ziyaret;

    protected PopulerMekanModel(Parcel in) {
        mekanID = in.readInt();
        mekanAd = in.readString();
        kapakResmi = in.readString();
        puan = in.readString();
        adresi = in.readString();
        ziyaret = in.readString();
    }

    public static final Creator<PopulerMekanModel> CREATOR = new Creator<PopulerMekanModel>() {
        @Override
        public PopulerMekanModel createFromParcel(Parcel in) {
            return new PopulerMekanModel(in);
        }

        @Override
        public PopulerMekanModel[] newArray(int size) {
            return new PopulerMekanModel[size];
        }
    };

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

    public String getKapakResmi() {
        return kapakResmi;
    }

    public void setKapakResmi(String kapakResmi) {
        this.kapakResmi = kapakResmi;
    }

    public String getPuan() {
        return puan;
    }

    public void setPuan(String puan) {
        this.puan = puan;
    }

    public String getAdresi() {
        return adresi;
    }

    public void setAdresi(String adresi) {
        this.adresi = adresi;
    }

    public String getZiyaret() {
        return ziyaret;
    }

    public void setZiyaret(String ziyaret) {
        this.ziyaret = ziyaret;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mekanID);
        parcel.writeString(mekanAd);
        parcel.writeString(kapakResmi);
        parcel.writeString(puan);
        parcel.writeString(adresi);
        parcel.writeString(ziyaret);
    }
}