package com.burakekmen.gurmeapp.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class YorumActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher{

    private String mail="", sifre="";

    private ImageView [] stars;
    private ImageView starBir, starIki, starUc, starDort, starBes;
    private Button btnYorumGonder=null;
    private String mekanID=null, mekanAd=null, yorum=null;
    private TextView txtMekanAd=null;
    private TextView txtYorum=null;

    ACProgressFlower dialogYorumYap;

    private int verilenPuan=3; // İlk açılışta 3 yıldız renkli geldiği için buraya default 3 puan verdim.
    private Boolean mekanSayfasındanMi=false;
    private String mekanPuani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorum);

       init();

    }

    private void init(){

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_logo);

        Bundle extras = getIntent().getExtras();
        mekanID = String.valueOf(extras.getInt("mekanID"));
        mekanAd = extras.getString("mekanAd");
        mekanSayfasındanMi = extras.getBoolean("mekanSayfasindan");
        mekanPuani = extras.getString("mekanPuani");

        SharedPreferences pref = this.getSharedPreferences("session",MODE_PRIVATE);
        mail = pref.getString("mail", null);
        sifre = pref.getString("sifre", null);

        txtMekanAd = (TextView) findViewById(R.id.activity_yorum_mekanAd);
        txtMekanAd.setText(mekanAd);

        txtYorum = (TextView) findViewById(R.id.activity_yorum_txtYorum);
        txtYorum.addTextChangedListener(this);

        btnYorumGonder = (Button) findViewById(R.id.activity_yorum_btnYorumYap);
        btnYorumGonder.setOnClickListener(this);

        starBir = (ImageView) findViewById(R.id.activity_yorum_starOne);
        starIki = (ImageView) findViewById(R.id.activity_yorum_starTwo);
        starUc = (ImageView) findViewById(R.id.activity_yorum_starThree);
        starDort = (ImageView) findViewById(R.id.activity_yorum_starThird);
        starBes = (ImageView) findViewById(R.id.activity_yorum_starFive);

        stars = new ImageView[]{starBir, starIki, starUc, starDort, starBes};

        for(int i=0; i< stars.length; i++){
            if(i < 3) {
                stars[i].setColorFilter((ContextCompat.getColor(this, R.color.yildizSeciliRenk)));
                stars[i].setOnClickListener(this);
            }
            else {
                stars[i].setColorFilter((ContextCompat.getColor(this, R.color.yildizDefaultRenk)));
                stars[i].setOnClickListener(this);
            }
        }


    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private void donguyuKur(int sirasi){
        for(int i=1;i<stars.length; i++){
            if(i <= sirasi)
                stars[i].setColorFilter((ContextCompat.getColor(this, R.color.yildizSeciliRenk)));
            else
                stars[i].setColorFilter((ContextCompat.getColor(this, R.color.yildizDefaultRenk)));
        }
    }

    private void YorumYap(){
        dialogYorumYap = new ACProgressFlower.Builder(YorumActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Yorum Yapılıyor...")
                .fadeColor(Color.DKGRAY).build();
        dialogYorumYap.show();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.gurmeapp.xyz/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService service = retrofit.create(ApiService.class);

        String gecerliMail;
        if(mail.trim().length()>0)
            gecerliMail = mail;
        else
            gecerliMail="null";

        service.getMekanaYorumYap("Sw51QWvmP9-hjk2830ALXzvyT", mekanID, gecerliMail, yorum, String.valueOf(verilenPuan)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    Boolean sonuc = Boolean.valueOf(response.body());
                    if (sonuc){
                        dialogYorumYap.dismiss();
                        Toast.makeText(YorumActivity.this, "Yorum Gönderildi", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(YorumActivity.this, MekanActivity.class);
                        intent.putExtra("mekanID", mekanID);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(YorumActivity.this, "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                        dialogYorumYap.dismiss();
                    }
                }else {
                    Toast.makeText(YorumActivity.this, "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                    dialogYorumYap.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(YorumActivity.this, "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                dialogYorumYap.dismiss();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(txtYorum.getText().length() >= 3 && !txtYorum.getText().equals(""))
            btnYorumGonder.setEnabled(true);
        else
            btnYorumGonder.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.activity_yorum_starOne:
                donguyuKur(0);
                verilenPuan = 1;
                break;
            case R.id.activity_yorum_starTwo:
                donguyuKur(1);
                verilenPuan = 2;
                break;
            case R.id.activity_yorum_starThree:
                donguyuKur(2);
                verilenPuan = 3;
                break;
            case R.id.activity_yorum_starThird:
                donguyuKur(3);
                verilenPuan = 4;
                break;
            case R.id.activity_yorum_starFive:
                donguyuKur(4);
                verilenPuan = 5;
                break;
            case R.id.activity_yorum_btnYorumYap:
                if(isOnline()) {
                    yorum = txtYorum.getText().toString();
                    YorumYap();
                }else
                    Toast.makeText(this, "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(mekanSayfasındanMi) {
            Intent intent = new Intent(YorumActivity.this, MekanActivity.class);
            intent.putExtra("mekanID", mekanID);

            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(YorumActivity.this, TumYorumlarActivity.class);
            intent.putExtra("mekanID", Integer.valueOf(mekanID));
            intent.putExtra("mekanAd", mekanAd);
            intent.putExtra("ortPuan", mekanPuani);

            startActivity(intent);
            finish();
        }
    }
}
