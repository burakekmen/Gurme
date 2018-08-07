package com.burakekmen.gurmeapp.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.network.ApiClient;
import com.burakekmen.gurmeapp.network.ApiService;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.PopulerMekanModel;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView splashLogo = null;
    private Button btnKontrol;
    private ArrayList<PopulerMekanModel> mekanListesi = null;
    private AVLoadingIndicatorView loadingAnimasyon=null;
    //ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        init();
        kontrolEt();
    }

    private void init(){
        splashLogo = findViewById(R.id.activity_splash_logo);
        btnKontrol = findViewById(R.id.activity_splash_btnKontrol);
        loadingAnimasyon = findViewById(R.id.activity_splash_loadingAnimation);

        //apiService = ApiClient.getClient().create(ApiService.class);

        btnKontrol.setOnClickListener(this);
    }


    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private void kontrolEt(){
        if (isOnline()) {
            btnKontrol.setVisibility(View.INVISIBLE);
            loadingAnimasyon.smoothToShow();
            PopulerMekanlariGetir();
        }
        else {
            Toast.makeText(this, "İnternet Bağlantınızı Kontrol Edin", Toast.LENGTH_LONG).show();
            loadingAnimasyon.setVisibility(View.INVISIBLE);
            btnKontrol.setVisibility(View.VISIBLE);
        }
    }



    private void PopulerMekanlariGetir(){

//        Gson gson = new GsonBuilder().create();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://api.gurmeapp.xyz/")
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();

        ApiService service = ApiClient.getClient().create(ApiService.class);

        service.getPopulerMekanlar("Sw51QWvmP9-hjk2830ALXzvyT").enqueue(new Callback<ArrayList<PopulerMekanModel>>() {
            @Override
            public void onResponse(Call<ArrayList<PopulerMekanModel>> call, Response<ArrayList<PopulerMekanModel>> response) {

                if(response.isSuccessful()) {

                    mekanListesi = new ArrayList<>();
                    mekanListesi = response.body();

                    final SharedPreferences pref = getApplicationContext().getSharedPreferences("session", MODE_PRIVATE);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putBoolean("ilkAcilisMi", true);
                    edit.apply();

                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putParcelableArrayListExtra("mekanListesi", mekanListesi);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(SplashActivity.this, "1: " + response.message(), Toast.LENGTH_SHORT).show();
                    loadingAnimasyon.smoothToHide();
                    btnKontrol.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PopulerMekanModel>> call, Throwable t) {
                loadingAnimasyon.smoothToHide();
                Toast.makeText(SplashActivity.this, "2: " + t.toString(), Toast.LENGTH_SHORT).show();
                btnKontrol.setVisibility(View.VISIBLE);
            }
        });

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_splash_btnKontrol:
                kontrolEt();
                break;
            default:
                break;
        }
    }

}
