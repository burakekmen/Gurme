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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private View fLayout;

    private EditText txtAd=null, txtSoyad=null, txtMail=null, txtSifre=null;
    private Button btnKayitOl=null;

    private String ad="", soyad="", mail="", sifre="";

    ACProgressFlower dialogKayitOl;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fLayout = inflater.inflate(R.layout.fragment_register, container, false);
        init();

        return fLayout;
    }


    private void init(){
        txtAd = fLayout.findViewById(R.id.fragment_register_txtAd);
        txtSoyad = fLayout.findViewById(R.id.fragment_register_txtSoyad);
        txtMail = fLayout.findViewById(R.id.fragment_register_txtMail);
        txtSifre = fLayout.findViewById(R.id.fragment_register_txtSifre);
        btnKayitOl = fLayout.findViewById(R.id.fragment_register_btnKayitOl);

        btnKayitOl.setOnClickListener(this);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    private boolean bilgileriKontrolEt(){
        if(!isEmpty(txtAd) && !isEmpty(txtSoyad) && isEmailValid(txtMail.getText().toString()) && !isEmpty(txtSifre))
            return true;
        else
            return false;
    }


    private void KayitOl(){
        dialogKayitOl = new ACProgressFlower.Builder(getContext())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Bilgiler Gönderiliyor...")
                .fadeColor(Color.DKGRAY).build();
        dialogKayitOl.show();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.gurmeapp.xyz/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService service = retrofit.create(ApiService.class);

        ad = txtAd.getText().toString();
        soyad = txtSoyad.getText().toString();
        mail = txtMail.getText().toString();
        sifre = txtSifre.getText().toString();

        service.getKayitOl("Sw51QWvmP9-hjk2830ALXzvyT", ad, soyad, mail, sifre).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {

                    boolean sonuc = Boolean.valueOf(response.body());

                    if (sonuc) {
                        //TODO Burada Giriş Bilgilerini Kaydediyorum
                        SharedPreferences pref = getContext().getSharedPreferences("session", MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putBoolean("isLogged", true);
                        edit.putString("mail", mail);
                        edit.putString("sifre", sifre);
                        edit.apply();

                        Toast.makeText(getContext(), "Hoş Geldiniz", Toast.LENGTH_SHORT).show();

                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        Fragment profileFragment = new ProfileFragment();
                        fragmentTransaction.add(R.id.activity_home_fragment_container, profileFragment);
                        fragmentTransaction.commit();
                    } else {
                        SharedPreferences pref = getContext().getSharedPreferences("session", MODE_PRIVATE);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putBoolean("isLogged", false);
                        edit.putString("mail", "");
                        edit.putString("sifre", "");
                        edit.apply();

                        Toast.makeText(getContext(), "Bilgileri Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
                    }

                    dialogKayitOl.dismiss();

                }else
                    Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_register_btnKayitOl:
                if(isOnline()) {
                    if (bilgileriKontrolEt())
                        KayitOl();
                    else
                        Toast.makeText(getContext(), "Bilgileri Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

    }
}
