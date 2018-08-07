package com.burakekmen.gurmeapp.ui.activitys;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.burakekmen.gurmeapp.adapters.mekanActivityAdapters.ViewPagerAdapter;
import com.burakekmen.gurmeapp.adapters.mekanActivityRcListAdapters.YorumRecycleListAdapter;
import com.burakekmen.gurmeapp.network.ApiService;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.MekanDetayModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.burakekmen.gurmeapp.R.id.activity_mekan_mapFragment;

public class MekanActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OnMapReadyCallback, GoogleMap.OnMapClickListener {

    ACProgressFlower dialogMekanYüklenmesi, dialogFavriEkle, dialogFavoriSil;
    private String mail = "", sifre = "";  //TODO : KULLANICI BİLGİLERİ TUTULUYOR
    private LinearLayout mekanLatyout = null;
    private String mekanID;
    private int dotscount;
    private ViewPager viewPager;
    private ImageView[] dots;
    private TextView mekanAdi=null, tanitimYazisi=null, mekanAdresi=null, mekanPuani=null, mekanZiyareti=null, tumYorumlar=null;
    private ImageView imgFavori=null, imgAra=null, imgYorum= null, puanicon=null, viewicon=null;
    private MekanDetayModel mekanDetayModel=null;
    private RecyclerView rcList = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int gorunenResim= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mekan);

        if (isOnline()) {
            init();
            MekanBilgileriniGetir();
        }else
            Toast.makeText(this, "İnternet Bağlantınızı Kontrol Ediniz!", Toast.LENGTH_SHORT).show();
    }

    private void init(){

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_logo);

        Bundle extras = getIntent().getExtras();
        mekanID = extras.getString("mekanID");

        SharedPreferences pref = this.getSharedPreferences("session",MODE_PRIVATE);
        mail = pref.getString("mail", null);
        sifre = pref.getString("sifre", null);

        mekanLatyout = findViewById(R.id.activity_mekan_layout);
        rcList = findViewById(R.id.activity_mekan_rcList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcList.setLayoutManager(linearLayoutManager);
        rcList.setItemAnimator(new DefaultItemAnimator());

        viewPager = findViewById(R.id.activity_mekan_viewPager);

        imgYorum = findViewById(R.id.activity_mekan_yorum);
        imgAra = findViewById(R.id.activity_mekan_ara);
        imgFavori = findViewById(R.id.activity_mekan_favori);
        mekanAdi = findViewById(R.id.activity_mekan_mekanAdi);
        tanitimYazisi = findViewById(R.id.activity_mekan_mekanTanitimYazisi);
        mekanAdresi = findViewById(R.id.activity_mekan_mekanAdresi);
        mekanPuani = findViewById(R.id.activity_mekan_mekanPuani);
        mekanZiyareti = findViewById(R.id.activity_mekan_mekanZiyareti);
        tumYorumlar = findViewById(R.id.activity_mekan_tumYorumlar);
        puanicon = findViewById(R.id.activity_mekan_puanicon);
        viewicon = findViewById(R.id.activity_mekan_mekanZiyaretIcon);

        puanicon.setColorFilter((ContextCompat.getColor(this, R.color.yildizSeciliRenk)));
        viewicon.setColorFilter((ContextCompat.getColor(this, R.color.facebookrengi)));

        imgYorum.setOnClickListener(this);
        imgAra.setOnClickListener(this);
        imgFavori.setOnClickListener(this);
        tumYorumlar.setOnClickListener(this);

        swipeRefreshLayout = findViewById(R.id.activity_mekan_swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    private void haritayıCagir() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(activity_mekan_mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(mekanDetayModel.getLati()), Double.valueOf(mekanDetayModel.getLonglati())), 14.5f));

        UiSettings uis = googleMap.getUiSettings();
        uis.setCompassEnabled(true);
        uis.setZoomControlsEnabled(false);
        uis.setMyLocationButtonEnabled(true);

        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(isOnline()) {
            Intent intent = new Intent(MekanActivity.this, MapsActivity.class);
            intent.putExtra("lati", Double.valueOf(mekanDetayModel.getLati()));
            intent.putExtra("longlati", Double.valueOf(mekanDetayModel.getLonglati()));
            intent.putExtra("mekanAd", mekanDetayModel.getMekanAd());

            startActivity(intent);
        }
        else{
            internetYokMesaji();
        }
    }

    private void internetYokMesaji(){
        Snackbar snackBar = Snackbar.make(mekanLatyout, "İnternet Bağlantınızı Kontrol Ediniz!", Snackbar.LENGTH_SHORT);
        snackBar.show();
    }

    private void listeyeGonder() {
        YorumRecycleListAdapter adapter = new YorumRecycleListAdapter(this, mekanDetayModel.getYorumlar());
        rcList.setAdapter(adapter);
    }

    private void resimleriViewPagerGonder() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, mekanDetayModel.getMekanResimsUrl());

        LinearLayout sliderDotspanel = findViewById(R.id.SliderDots);
        sliderDotspanel.removeAllViews();

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for (int i = 0; i < dotscount; i++) {

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                gorunenResim = position;

                for (int i = 0; i < dotscount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setAdapter(viewPagerAdapter);
    }

    private void verileriDoldur() {

        if (mekanDetayModel.getFavori())
            imgFavori.setImageResource(R.mipmap.ic_favorite_icon);

        mekanAdi.setText(mekanDetayModel.getMekanAd());
        tanitimYazisi.setText(mekanDetayModel.getMekanTanitimYazisi());
        mekanAdresi.setText(mekanDetayModel.getMekanAdres());

        if (mekanDetayModel.getMekanPuan().equals("-1"))
            mekanDetayModel.setMekanPuan("Oy Yok");

        mekanPuani.setText(mekanDetayModel.getMekanPuan());
        mekanZiyareti.setText(mekanDetayModel.getMekanZiyaret());
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                }
                return;
            }
        }
    }

    public boolean checkCallPermission()
    {
        String permission = "android.permission.CALL_PHONE";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void aramaYap() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Arama Yapılsın Mı?");
        alertDialogBuilder
                .setMessage(mekanDetayModel.getTelefon())
                .setCancelable(false)
                .setPositiveButton("Ara",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Intent.ACTION_CALL);

                                intent.setData(Uri.parse("tel:" + mekanDetayModel.getTelefon()));
                                if (ActivityCompat.checkSelfPermission(MekanActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    return;
                                }
                                startActivity(intent);
                            }
                        })

                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_mekan_favori:
                if(isOnline()) {
                    if (!mail.equals("")) {
                        if (mekanDetayModel.getFavori())
                            FavoridenCikart();
                        else
                            FavoriyeEkle();
                    } else {
                        Snackbar snackBar = Snackbar.make(mekanLatyout, "Favoriye Eklemek İçin Giriş Yapmalısınız!", Snackbar.LENGTH_SHORT);
                        snackBar.show();
                    }
                }else
                    internetYokMesaji();
                break;
            case R.id.activity_mekan_ara:
                if (!checkCallPermission()) {
                    Snackbar snackBar = Snackbar.make(mekanLatyout, "Arama Özelliği İçin İZİN Verilmelidir!", Snackbar.LENGTH_SHORT);
                    snackBar.show();

                    ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.CALL_PHONE}, 1);
                }
                else
                    aramaYap();
                break;
            case R.id.activity_mekan_yorum:
                if(isOnline()) {
                    if (!mail.equals("")) {
                        Intent intent = new Intent(MekanActivity.this, YorumActivity.class);
                        intent.putExtra("mekanID", mekanDetayModel.getMekanID());
                        intent.putExtra("mekanAd", mekanDetayModel.getMekanAd());
                        intent.putExtra("mekanSayfasindan", true);
                        intent.putExtra("mekanPuani", "0");

                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar snackBar = Snackbar.make(mekanLatyout, "Yorum Yazabilmek İçin Giriş Yapmalısınız!", Snackbar.LENGTH_SHORT);
                        snackBar.show();
                    }
                }else
                    internetYokMesaji();
                break;
            case R.id.activity_mekan_tumYorumlar:
                if(isOnline()) {
                    Intent intent = new Intent(MekanActivity.this, TumYorumlarActivity.class);
                    intent.putExtra("mekanID", mekanDetayModel.getMekanID());
                    intent.putExtra("mekanAd", mekanDetayModel.getMekanAd());
                    intent.putExtra("ortPuan", mekanDetayModel.getMekanPuan());

                    startActivity(intent);
                    finish();
                }else
                    internetYokMesaji();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        if(isOnline()) {
            Snackbar snackBar = Snackbar.make(mekanLatyout, "Yenileniyor...", Snackbar.LENGTH_SHORT);
            snackBar.show();
            MekanBilgileriniGetir();
        }else{
            internetYokMesaji();
            swipeRefreshLayout.setRefreshing(false);
        }

    }


    private void MekanBilgileriniGetir(){
        swipeRefreshLayout.setRefreshing(true);

        dialogMekanYüklenmesi = new ACProgressFlower.Builder(MekanActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Bilgiler Alınıyor...")
                .fadeColor(Color.DKGRAY).build();
        dialogMekanYüklenmesi.show();

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

        service.getMekanDetayBilgileri("Sw51QWvmP9-hjk2830ALXzvyT", mekanID, gecerliMail).enqueue(new Callback<MekanDetayModel>() {
            @Override
            public void onResponse(Call<MekanDetayModel> call, Response<MekanDetayModel> response) {
                if(response.isSuccessful()){
                    mekanDetayModel = new MekanDetayModel();
                    mekanDetayModel = response.body();

                    verileriDoldur();
                    resimleriViewPagerGonder();
                    listeyeGonder();
                    haritayıCagir();
                    swipeRefreshLayout.setRefreshing(false);
                    dialogMekanYüklenmesi.dismiss();

                }else {
                    Snackbar snackBar = Snackbar.make(mekanLatyout, "Bir şeyler tes gitti!", Snackbar.LENGTH_SHORT);
                    snackBar.show();
                    swipeRefreshLayout.setRefreshing(false);
                    dialogMekanYüklenmesi.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MekanDetayModel> call, Throwable t) {
                Snackbar snackBar = Snackbar.make(mekanLatyout, "Bir şeyler Ters Gitti!", Snackbar.LENGTH_SHORT);
                snackBar.show();

                swipeRefreshLayout.setRefreshing(false);
                dialogMekanYüklenmesi.dismiss();
            }
        });


    }


    private void FavoriyeEkle(){
        dialogFavriEkle = new ACProgressFlower.Builder(MekanActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Favorilere Ekleniyor...")
                .fadeColor(Color.DKGRAY).build();
        dialogFavriEkle.show();

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

        service.getFavoriyeEkle("Sw51QWvmP9-hjk2830ALXzvyT", mekanID, gecerliMail).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){

                    Boolean sonuc = Boolean.valueOf(response.body());

                    if(sonuc) {
                        mekanDetayModel.setFavori(true);
                        imgFavori.setImageResource(R.mipmap.ic_favorite_icon);

                        Snackbar snackBar = Snackbar.make(mekanLatyout, "Favoriye Eklendi!", Snackbar.LENGTH_SHORT);
                        snackBar.show();

                        dialogFavriEkle.dismiss();
                    }else {
                        Snackbar snackBar = Snackbar.make(mekanLatyout, "Bir şeyler Ters Gitti!", Snackbar.LENGTH_SHORT);
                        snackBar.show();
                        dialogFavriEkle.dismiss();
                    }

                }else {
                    Snackbar snackBar = Snackbar.make(mekanLatyout, "Bir şeyler Ters Gitti!", Snackbar.LENGTH_SHORT);
                    snackBar.show();
                    dialogFavriEkle.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar snackBar = Snackbar.make(mekanLatyout, "Bir şeyler Ters Gitti!", Snackbar.LENGTH_SHORT);
                snackBar.show();
                dialogFavriEkle.dismiss();
            }
        });

    }


    private void FavoridenCikart(){
        dialogFavoriSil = new ACProgressFlower.Builder(MekanActivity.this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Favorilerden Çıkartılıyor...")
                .fadeColor(Color.DKGRAY).build();
        dialogFavoriSil.show();

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

        service.getFavoridenCikart("Sw51QWvmP9-hjk2830ALXzvyT", mekanID, gecerliMail).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){

                    Boolean sonuc = Boolean.valueOf(response.body());

                    if(sonuc) {
                        mekanDetayModel.setFavori(false);
                        imgFavori.setImageResource(R.mipmap.ic_unfavorite_icon);

                        Snackbar snackBar = Snackbar.make(mekanLatyout, "Favorilerden Çıkarıldı!", Snackbar.LENGTH_SHORT);
                        snackBar.show();

                        dialogFavoriSil.dismiss();
                    }else {
                        Snackbar snackBar = Snackbar.make(mekanLatyout, "Bir şeyler Ters Gitti!", Snackbar.LENGTH_SHORT);
                        snackBar.show();

                        dialogFavoriSil.dismiss();
                    }

                }else {
                    Snackbar snackBar = Snackbar.make(mekanLatyout, "Bir şeyler Ters Gitti!", Snackbar.LENGTH_SHORT);
                    snackBar.show();

                    dialogFavoriSil.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar snackBar = Snackbar.make(mekanLatyout, "Bir şeyler Ters Gitti!", Snackbar.LENGTH_SHORT);
                snackBar.show();
                dialogFavoriSil.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
