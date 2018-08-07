package com.burakekmen.gurmeapp.ui.fragments;


import android.content.Context;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class RememberPasswordFragment extends Fragment implements View.OnClickListener {

    private View fLayout;

    private EditText txtMail=null;
    private Button btnParolaHatirlat=null;

    private String mail="";

    ACProgressFlower dialogParolaHatirlat;

    public RememberPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fLayout = inflater.inflate(R.layout.fragment_remember_password, container, false);
        init();

        return fLayout;
    }


    private void init(){
        txtMail = fLayout.findViewById(R.id.fragment_remember_password_txtMail);
        btnParolaHatirlat = fLayout.findViewById(R.id.fragment_remember_password_btnParolaHatirlat);

        btnParolaHatirlat.setOnClickListener(this);
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

    private boolean bilgileriKontrolEt(){
        if(isEmailValid(txtMail.getText().toString()))
            return true;
        else
            return false;
    }


    private void ParolaHatirlat(){
        dialogParolaHatirlat = new ACProgressFlower.Builder(getContext())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Bilgiler Kontrol Ediliyor...")
                .fadeColor(Color.DKGRAY).build();
        dialogParolaHatirlat.show();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.gurmeapp.xyz/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService service = retrofit.create(ApiService.class);

        mail = txtMail.getText().toString();

        service.getParolaHatirlat("Sw51QWvmP9-hjk2830ALXzvyT", mail).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {

                    boolean sonuc = Boolean.valueOf(response.body());

                    if (sonuc) {
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        Fragment loginFragment = new LoginFragment();
                        fragmentTransaction.add(R.id.activity_home_fragment_container, loginFragment);
                        fragmentTransaction.commit();

                        Toast.makeText(getContext(), "Şifreniz Mail Hesabınıza Gönderildi!", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(getContext(), "Bilgilerini Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
                    dialogParolaHatirlat.dismiss();
                }else {
                    Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                    dialogParolaHatirlat.dismiss();
                }
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
            case R.id.fragment_remember_password_btnParolaHatirlat:
                if(isOnline()) {
                    if (bilgileriKontrolEt())
                        ParolaHatirlat();
                    else
                        Toast.makeText(getContext(), "Mail Adresinizi Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getContext(), "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
