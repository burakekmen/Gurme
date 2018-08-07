package com.burakekmen.gurmeapp.network.retrofit.gsonModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/****************************
 * Created by Burak EKMEN   |
 * 27.10.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class ProfilModel {

    @SerializedName("uyeBilgileri")
    @Expose
    private UyeBilgileri uyeBilgileri;
    @SerializedName("yorumlar")
    @Expose
    private ArrayList<Yorumlar> yorumlar = null;

    public UyeBilgileri getUyeBilgileri() {
        return uyeBilgileri;
    }

    public void setUyeBilgileri(UyeBilgileri uyeBilgileri) {
        this.uyeBilgileri = uyeBilgileri;
    }

    public ArrayList<Yorumlar> getYorumlar() {
        return yorumlar;
    }

    public void setYorumlar(ArrayList<Yorumlar> yorumlar) {
        this.yorumlar = yorumlar;
    }
}
