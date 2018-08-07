package com.burakekmen.gurmeapp.ui.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.adapters.profileYorumRcListAdapters.ProfileYorumRecycleListAdapter;
import com.burakekmen.gurmeapp.network.ApiService;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.ProfilModel;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
public class ProfileFragment extends Fragment implements View.OnClickListener, OnActiveListener {

    ACProgressFlower dialogProfilYuklenmesi;
    private ProfilModel profilBilgileri;
    private View fLayout;
    private String mail="";
    private EditText txtAd = null, txtSoyad = null, txtMail = null;
    private RecyclerView rcList;
    private SwipeButton btnCikisYap = null;

    public ProfileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fLayout = inflater.inflate(R.layout.fragment_profile, container, false);
        if (isOnline()) {
            init();
            ProfilBilgileriniGetir();

        }else
            Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();

        return fLayout;
    }

    private void init(){
        SharedPreferences pref = getContext().getSharedPreferences("session", MODE_PRIVATE);
        mail = pref.getString("mail", null);

        txtAd = fLayout.findViewById(R.id.fragment_profile_txtAd);
        txtSoyad = fLayout.findViewById(R.id.fragment_profile_txtSoyad);
        txtMail = fLayout.findViewById(R.id.fragment_profile_txtMail);

        rcList = fLayout.findViewById(R.id.fragment_profile_rcList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcList.setLayoutManager(linearLayoutManager);
        rcList.setItemAnimator(new DefaultItemAnimator());
        rcList.setHasFixedSize(true);

        btnCikisYap = fLayout.findViewById(R.id.fragment_profile_btnCikisYap);
        btnCikisYap.setOnActiveListener(this);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void listeyeGonder() {
        ProfileYorumRecycleListAdapter adapter = new ProfileYorumRecycleListAdapter(getContext(), profilBilgileri.getYorumlar());
        rcList.setAdapter(adapter);
    }

    @Override
    public void onActive() {
        SharedPreferences pref = getContext().getSharedPreferences("session",MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("isLogged", false);
        edit.putString("mail", "");
        edit.putString("sifre", "");
        edit.apply();

        Toast.makeText(getContext(), "Çıkış Yapıldı", Toast.LENGTH_SHORT).show();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.activity_home_fragment_container, loginFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            default:
                break;
        }
    }


    private void ProfilBilgileriniGetir() {

        dialogProfilYuklenmesi = new ACProgressFlower.Builder(getContext())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Bilgiler Alınıyor...")
                .fadeColor(Color.DKGRAY).build();
        dialogProfilYuklenmesi.show();

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

        service.getUyeProfil("Sw51QWvmP9-hjk2830ALXzvyT", gecerliMail).enqueue(new Callback<ProfilModel>() {
            @Override
            public void onResponse(Call<ProfilModel> call, Response<ProfilModel> response) {
                if(response.isSuccessful()){
                    profilBilgileri = new ProfilModel();
                    profilBilgileri = response.body();

                    txtAd.setText(profilBilgileri.getUyeBilgileri().getAd());
                    txtSoyad.setText(profilBilgileri.getUyeBilgileri().getSoyad());
                    txtMail.setText(profilBilgileri.getUyeBilgileri().getMail());

                    listeyeGonder();

                    dialogProfilYuklenmesi.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ProfilModel> call, Throwable t) {

            }
        });
    }

}
