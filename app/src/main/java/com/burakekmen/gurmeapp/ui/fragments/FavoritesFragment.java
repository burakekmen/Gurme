package com.burakekmen.gurmeapp.ui.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.adapters.populerFragmentRcListAdapters.RecycleListAdapter;
import com.burakekmen.gurmeapp.network.ApiService;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.PopulerMekanModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressBaseDialog;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private String mail="", sifre="";
    private View fLayout;
    private RecyclerView rcList = null;
    private ArrayList<PopulerMekanModel> favoriMekanlar = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private ACProgressBaseDialog dialogFavoriYuklenmesi;

    public FavoritesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fLayout = inflater.inflate(R.layout.fragment_favorites, container, false);
        if(isOnline()) {
            init();
            FavorileriGetir();
        }else
            Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
        return fLayout;
    }

    private void init(){

        swipeRefreshLayout = fLayout.findViewById(R.id.fragment_favorites_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        rcList = fLayout.findViewById(R.id.fragment_favorites_rcList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcList.setLayoutManager(linearLayoutManager);
        rcList.setItemAnimator(new DefaultItemAnimator());
        rcList.setHasFixedSize(true);

        SharedPreferences pref = getContext().getSharedPreferences("session",MODE_PRIVATE);
        mail = pref.getString("mail", null);
        sifre = pref.getString("sifre", null);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void listeyeGonder() {
        RecycleListAdapter adapter = new RecycleListAdapter(getContext(), favoriMekanlar);
        rcList.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        if (isOnline()) {
            Toast.makeText(getContext(), "Yenileniyor", Toast.LENGTH_SHORT).show();
            FavorileriGetir();
        }else{
            Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    private void FavorileriGetir() {

        swipeRefreshLayout.setRefreshing(true);

        dialogFavoriYuklenmesi = new ACProgressFlower.Builder(getContext())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Bilgiler Alınıyor...")
                .fadeColor(Color.DKGRAY).build();
        dialogFavoriYuklenmesi.show();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.gurmeapp.xyz/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService service = retrofit.create(ApiService.class);

        String gecerliMail;
        if(mail.isEmpty())
            gecerliMail="null";
        else
            gecerliMail = mail;

        service.getFavoriler("Sw51QWvmP9-hjk2830ALXzvyT", gecerliMail).enqueue(new Callback<ArrayList<PopulerMekanModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PopulerMekanModel>> call, Response<ArrayList<PopulerMekanModel>> response) {

                if (response.isSuccessful()) {

                    favoriMekanlar = new ArrayList<PopulerMekanModel>();
                    favoriMekanlar = response.body();

                    if (!mail.equals(""))
                        listeyeGonder();
                    else
                        Toast.makeText(getContext(), "Favoriler için Giriş Yapmalısınız!", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                    dialogFavoriYuklenmesi.dismiss();
                } else {
                    Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                    dialogFavoriYuklenmesi.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PopulerMekanModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                dialogFavoriYuklenmesi.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
