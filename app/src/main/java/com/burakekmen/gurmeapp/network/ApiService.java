package com.burakekmen.gurmeapp.network;

import com.burakekmen.gurmeapp.network.retrofit.gsonModels.MekanDetayModel;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.PopulerMekanModel;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.ProfilModel;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.TumYorumlarModel;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.YakindakiMekanlarModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/****************************
 * Created by Burak EKMEN   |
 * 27.10.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public interface ApiService {

    @GET("populerMekanlar")
    Call<ArrayList<PopulerMekanModel>> getPopulerMekanlar(@Query("key") String key);

    @GET("aramaYap")
    Call<ArrayList<PopulerMekanModel>> getAramaYap(@Query("key") String key, @Query("aramaMetni") String aramaMetni);

    @GET("favorileriCek")
    Call<ArrayList<PopulerMekanModel>> getFavoriler(@Query("key") String key, @Query("mail") String mail);

    @GET("uyeProfil")
    Call<ProfilModel> getUyeProfil(@Query("key") String key, @Query("mail") String mail);

    @GET("girisYap")
    Call<String> getGirisYap(@Query("key") String key, @Query("mail") String mail, @Query("sifre") String sifre);

    @GET("kayitOl")
    Call<String> getKayitOl(@Query("key") String key, @Query("ad") String ad, @Query("soyad") String soyad, @Query("mail") String mail, @Query("sifre") String sifre);

    @GET("parolamiUnuttum")
    Call<String> getParolaHatirlat(@Query("key") String key, @Query("mail") String mail);

    @GET("yakindakiMekanlar")
    Call<ArrayList<YakindakiMekanlarModel>> getYakindakiMekanlar(@Query("key") String key, @Query("lati") String lati, @Query("longlati") String longlati);

    @GET("mekanBilgileriniCek")
    Call<MekanDetayModel> getMekanDetayBilgileri(@Query("key") String key, @Query("mekanID") String mekanID, @Query("mail") String mail);

    @GET("favoriyeEkle")
    Call<String> getFavoriyeEkle(@Query("key") String key, @Query("mekanID") String mekanID, @Query("mail") String mail);

    @GET("favoridenSil")
    Call<String> getFavoridenCikart(@Query("key") String key, @Query("mekanID") String mekanID, @Query("mail") String mail);

    @GET("mekanTumYorumlar")
    Call<ArrayList<TumYorumlarModel>> getTumYorumlar(@Query("key") String key, @Query("mekanID") String mekanID);

    @GET("mekanaYorumYap")
    Call<String> getMekanaYorumYap(@Query("key") String key, @Query("mekanID") String mekanID, @Query("mail") String mail, @Query("yorum") String yorum, @Query("puan") String puan);

}
