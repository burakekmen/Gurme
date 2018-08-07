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
public class PopulerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ACProgressFlower dialogListeYuklenmesi;
    private View fLayout;
    private RecyclerView rcList = null;
    private ArrayList<PopulerMekanModel> mekanListesi = null;
    private SwipeRefreshLayout swipeRefreshLayout = null;

    public PopulerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fLayout = inflater.inflate(R.layout.fragment_populer, container, false);
        init();
        if(isOnline()) {
            final SharedPreferences pref = getContext().getSharedPreferences("session", MODE_PRIVATE);
            if (pref.getBoolean("ilkAcilisMi", true)) { // TODO: TRUE İSE
                SharedPreferences.Editor edit = pref.edit();
                edit.putBoolean("ilkAcilisMi", false);

                edit.apply();

                Bundle bundle = this.getArguments();
                if (bundle != null) {
                    mekanListesi = bundle.getParcelableArrayList("mekanListesi");
                    if(mekanListesi != null)
                        listeyeGonder();
                }else
                    PopulerMekanlariGetir();
            }else {
                PopulerMekanlariGetir();
            }
        }
        else
            Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();

        return fLayout;
    }

    private void init() {

        swipeRefreshLayout = fLayout.findViewById(R.id.fragment_populer_swipeRefreshLayout);

        rcList = fLayout.findViewById(R.id.fragment_populer_rcList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcList.setLayoutManager(linearLayoutManager);
        rcList.setItemAnimator(new DefaultItemAnimator());
        rcList.setHasFixedSize(true);

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void listeyeGonder() {
        RecycleListAdapter adapter = new RecycleListAdapter(getContext(), mekanListesi);
        rcList.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        if(isOnline()) {
            Toast.makeText(getContext(), "Yenileniyor", Toast.LENGTH_SHORT).show();
            PopulerMekanlariGetir();
            swipeRefreshLayout.setRefreshing(false);
        }else{
            Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    private void PopulerMekanlariGetir(){
        swipeRefreshLayout.setRefreshing(true);

        dialogListeYuklenmesi = new ACProgressFlower.Builder(getContext())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Bilgiler Alınıyor...")
                .fadeColor(Color.DKGRAY).build();
        dialogListeYuklenmesi.show();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.gurmeapp.xyz/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.getPopulerMekanlar("Sw51QWvmP9-hjk2830ALXzvyT").enqueue(new Callback<ArrayList<PopulerMekanModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PopulerMekanModel>> call, Response<ArrayList<PopulerMekanModel>> response) {

                if(response.isSuccessful()) {

                    mekanListesi = new ArrayList<PopulerMekanModel>();
                    mekanListesi = response.body();

                    listeyeGonder();
                    swipeRefreshLayout.setRefreshing(false);
                    dialogListeYuklenmesi.dismiss();
                }else {
                    Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                    dialogListeYuklenmesi.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PopulerMekanModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                dialogListeYuklenmesi.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


}