package com.burakekmen.gurmeapp.ui.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, TextWatcher {

    private View fLayout;
    private SearchView aramaCubugu;
    private RecyclerView rcList = null;
    private ArrayList<PopulerMekanModel> mekanListesi = null;

    private String aramaMetni;
    private SwipeRefreshLayout swipeRefreshLayout;

    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fLayout = inflater.inflate(R.layout.fragment_search, container, false);
        init();

        return fLayout;
    }


    private void init(){

        mekanListesi = new ArrayList<>();

        swipeRefreshLayout = fLayout.findViewById(R.id.fragment_search_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        rcList = fLayout.findViewById(R.id.fragment_search_rcList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcList.setLayoutManager(linearLayoutManager);
        rcList.setItemAnimator(new DefaultItemAnimator());
        rcList.setHasFixedSize(true);

        aramaCubugu = fLayout.findViewById(R.id.fragment_search_aramaCubugu);
        aramaCubugu.setOnClickListener(this);
        aramaCubugu.setOnQueryTextListener(this);
        aramaCubugu.onActionViewExpanded();
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mekanListesi.clear();
        aramaMetni = query;

        if(isOnline()) {
            if (!aramaMetni.equals(""))
                AramaYap();
        }else
            Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();

        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mekanListesi.clear();
        aramaMetni = newText;

        if(isOnline()) {
            if (!aramaMetni.equals("") && !aramaMetni.isEmpty() && !aramaMetni.equals(null) && aramaMetni.trim().length()>0)
                AramaYap();
        }else {
            Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void listeyeGonder() {
        RecycleListAdapter adapter = new RecycleListAdapter(getContext(), mekanListesi);
        rcList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_search_aramaCubugu:
                aramaCubugu.setIconified(false);
                aramaCubugu.clearFocus();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        if(isOnline()) {
            AramaYap();
            Toast.makeText(getContext(), "Yenileniyor", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }



    private void AramaYap(){
        swipeRefreshLayout.setRefreshing(true);

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.gurmeapp.xyz/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.getAramaYap("Sw51QWvmP9-hjk2830ALXzvyT", aramaMetni).enqueue(new Callback<ArrayList<PopulerMekanModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PopulerMekanModel>> call, Response<ArrayList<PopulerMekanModel>> response) {

                if (response.isSuccessful()) {

                    mekanListesi = new ArrayList<>();
                    mekanListesi = response.body();

                    listeyeGonder();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PopulerMekanModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
