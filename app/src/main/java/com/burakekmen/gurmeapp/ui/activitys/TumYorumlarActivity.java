package com.burakekmen.gurmeapp.ui.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.adapters.tumYorumlarRcListAdapters.TumYorumlarRecycleListAdapter;
import com.burakekmen.gurmeapp.network.ApiService;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.TumYorumlarModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.burakekmen.gurmeapp.R.id.fab;

public class TumYorumlarActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private String mail="", sifre="";
    private LinearLayout star5, star4, star3, star2, star1;
    private String mekanID= null, mekanAd=null, ortPuan=null;
    private TextView txtOrtPuan=null;
    private SwipeRefreshLayout swipeRefreshLayout = null;

    private ArrayList<TumYorumlarModel> tumYorumlarListesi;
    private RecyclerView rcList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tum_yorumlar);

        if(isOnline()) {
            init();
            TumYorumlariGetir();
        }else
            Toast.makeText(this, "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();

    }

    private void init(){

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_logo);

        Bundle extras = getIntent().getExtras();
        mekanID = String.valueOf(extras.getInt("mekanID"));
        mekanAd = extras.getString("mekanAd");
        ortPuan = extras.getString("ortPuan");

        SharedPreferences pref = this.getSharedPreferences("session",MODE_PRIVATE);
        mail = pref.getString("mail", null);
        sifre = pref.getString("sifre", null);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_tum_yorumlar_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        txtOrtPuan = (TextView) findViewById(R.id.activity_tumyorumlar_txtOrtPuan);
        txtOrtPuan.setText(ortPuan);

        star5 = (LinearLayout) findViewById(R.id.tum_yorumlar_layoutStar5);
        star4 = (LinearLayout) findViewById(R.id.tum_yorumlar_layoutStar4);
        star3 = (LinearLayout) findViewById(R.id.tum_yorumlar_layoutStar3);
        star2 = (LinearLayout) findViewById(R.id.tum_yorumlar_layoutStar2);
        star1 = (LinearLayout) findViewById(R.id.tum_yorumlar_layoutStar1);

        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);

        yildizRenklendir();

        rcList = (RecyclerView) findViewById(R.id.activity_tumyorumlar_rcList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcList.setLayoutManager(linearLayoutManager);
        rcList.setItemAnimator(new DefaultItemAnimator());
        rcList.setHasFixedSize(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public void  yildizRenklendir() {

        ImageView star51 = (ImageView) findViewById(R.id.content_tum_yorumlar_star51);
        ImageView star52 = (ImageView) findViewById(R.id.content_tum_yorumlar_star52);
        ImageView star53 = (ImageView) findViewById(R.id.content_tum_yorumlar_star53);
        ImageView star54 = (ImageView) findViewById(R.id.content_tum_yorumlar_star54);
        ImageView star55 = (ImageView) findViewById(R.id.content_tum_yorumlar_star55);

        ImageView star41 = (ImageView) findViewById(R.id.content_tum_yorumlar_star41);
        ImageView star42 = (ImageView) findViewById(R.id.content_tum_yorumlar_star42);
        ImageView star43 = (ImageView) findViewById(R.id.content_tum_yorumlar_star43);
        ImageView star44 = (ImageView) findViewById(R.id.content_tum_yorumlar_star44);
        ImageView star45 = (ImageView) findViewById(R.id.content_tum_yorumlar_star45);

        ImageView star31 = (ImageView) findViewById(R.id.content_tum_yorumlar_star31);
        ImageView star32 = (ImageView) findViewById(R.id.content_tum_yorumlar_star32);
        ImageView star33 = (ImageView) findViewById(R.id.content_tum_yorumlar_star33);
        ImageView star34 = (ImageView) findViewById(R.id.content_tum_yorumlar_star34);
        ImageView star35 = (ImageView) findViewById(R.id.content_tum_yorumlar_star35);

        ImageView star21 = (ImageView) findViewById(R.id.content_tum_yorumlar_star21);
        ImageView star22 = (ImageView) findViewById(R.id.content_tum_yorumlar_star22);
        ImageView star23 = (ImageView) findViewById(R.id.content_tum_yorumlar_star23);
        ImageView star24 = (ImageView) findViewById(R.id.content_tum_yorumlar_star24);
        ImageView star25 = (ImageView) findViewById(R.id.content_tum_yorumlar_star25);

        ImageView star11 = (ImageView) findViewById(R.id.content_tum_yorumlar_star11);
        ImageView star12 = (ImageView) findViewById(R.id.content_tum_yorumlar_star12);
        ImageView star13 = (ImageView) findViewById(R.id.content_tum_yorumlar_star13);
        ImageView star14 = (ImageView) findViewById(R.id.content_tum_yorumlar_star14);
        ImageView star15 = (ImageView) findViewById(R.id.content_tum_yorumlar_star15);


        ImageView[] stars = new ImageView[]{star51, star52, star53, star54, star55,
                star41, star42, star43, star44, star45,
                star31, star32, star33, star34, star35,
                star21, star22, star23, star24, star25,
                star11, star12, star13, star14, star15};

        for (int i = 0; i < stars.length; i++) {
            if (i <= 8)
                stars[i].setColorFilter((ContextCompat.getColor(this, R.color.yildizSeciliRenk)));
            else if (i >= 10 & i <= 12)
                stars[i].setColorFilter((ContextCompat.getColor(this, R.color.yildizSeciliRenk)));
            else if (i >= 15 & i <= 16)
                stars[i].setColorFilter((ContextCompat.getColor(this, R.color.yildizSeciliRenk)));
            else if (i == 20)
                stars[i].setColorFilter((ContextCompat.getColor(this, R.color.yildizSeciliRenk)));
            else
                stars[i].setColorFilter((ContextCompat.getColor(this, R.color.yildizDefaultRenk)));
        }
    }


    private void TumYorumlariGetir(){
        swipeRefreshLayout.setRefreshing(true);

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.gurmeapp.xyz/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService service = retrofit.create(ApiService.class);

        service.getTumYorumlar("Sw51QWvmP9-hjk2830ALXzvyT", mekanID).enqueue(new Callback<ArrayList<TumYorumlarModel>>() {
            @Override
            public void onResponse(Call<ArrayList<TumYorumlarModel>> call, Response<ArrayList<TumYorumlarModel>> response) {
                if(response.isSuccessful()){

                    tumYorumlarListesi = new ArrayList<TumYorumlarModel>();
                    tumYorumlarListesi = response.body();

                    listeyeGonder(tumYorumlarListesi);
                    swipeRefreshLayout.setRefreshing(false);
                }else {
                    Toast.makeText(TumYorumlarActivity.this, "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<TumYorumlarModel>> call, Throwable t) {
                Toast.makeText(TumYorumlarActivity.this, "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void listeyeGonder(ArrayList<TumYorumlarModel> yorumListesi){
        TumYorumlarRecycleListAdapter adapter = new TumYorumlarRecycleListAdapter(this, yorumListesi);
        rcList.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case fab:
                if(isOnline()) {
                    if (!mail.equals("")) {
                        Intent intent = new Intent(TumYorumlarActivity.this, YorumActivity.class);
                        intent.putExtra("mekanID", Integer.valueOf(mekanID));
                        intent.putExtra("mekanAd", mekanAd);
                        intent.putExtra("mekanSayfasindan", false);
                        intent.putExtra("mekanPuani", ortPuan);

                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(this, "Yorum Yazabilmek İçin Giriş Yapmalısınız!", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(this, "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tum_yorumlar_layoutStar5:
                ArrayList<TumYorumlarModel> besYildizYorumlar = new ArrayList<>();

                for (TumYorumlarModel yorum: tumYorumlarListesi) {
                    if(yorum.getPuan().equals("5"))
                        besYildizYorumlar.add(yorum);
                }

                listeyeGonder(besYildizYorumlar);
                break;
            case R.id.tum_yorumlar_layoutStar4:
                ArrayList<TumYorumlarModel> dortYildizYorumlar = new ArrayList<>();

                for (TumYorumlarModel yorum: tumYorumlarListesi) {
                    if(yorum.getPuan().equals("4"))
                        dortYildizYorumlar.add(yorum);
                }

                listeyeGonder(dortYildizYorumlar);
                break;
            case R.id.tum_yorumlar_layoutStar3:
                ArrayList<TumYorumlarModel> ucYildizYorumlar = new ArrayList<>();

                for (TumYorumlarModel yorum: tumYorumlarListesi) {
                    if(yorum.getPuan().equals("3"))
                        ucYildizYorumlar.add(yorum);
                }

                listeyeGonder(ucYildizYorumlar);
                break;
            case R.id.tum_yorumlar_layoutStar2:
                ArrayList<TumYorumlarModel> ikiYildizYorumlar = new ArrayList<>();

                for (TumYorumlarModel yorum: tumYorumlarListesi) {
                    if(yorum.getPuan().equals("2"))
                        ikiYildizYorumlar.add(yorum);
                }

                listeyeGonder(ikiYildizYorumlar);
                break;
            case R.id.tum_yorumlar_layoutStar1:
                ArrayList<TumYorumlarModel> birYildizYorumlar = new ArrayList<>();

                for (TumYorumlarModel yorum: tumYorumlarListesi) {
                    if(yorum.getPuan().equals("1"))
                        birYildizYorumlar.add(yorum);
                }

                listeyeGonder(birYildizYorumlar);
                break;
            default:
                break;
        }
    }


    @Override
    public void onRefresh() {
        if(isOnline()) {
            TumYorumlariGetir();
            Toast.makeText(this, "Yenileniyor", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TumYorumlarActivity.this, MekanActivity.class);
        intent.putExtra("mekanID", mekanID);

        startActivity(intent);
        finish();
    }

}
