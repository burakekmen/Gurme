package com.burakekmen.gurmeapp.ui.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.adapters.GpsBul;
import com.burakekmen.gurmeapp.network.ApiService;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.YakindakiMekanlarModel;
import com.burakekmen.gurmeapp.ui.activitys.YolTarifiActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import static com.burakekmen.gurmeapp.R.id.fragment_nearby_places_mapFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearbyPlacesFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    GpsBul gps;
    GoogleMap mGoogleMap;
    double latitude, longlatitude, hedefLati, hedefLonglati;
    String lati,longlati;
    SupportMapFragment mapFragment;

    private FrameLayout fragment_nearby_places_layout;
    private View fLayout;
    private TextView txtMekanAd=null;

    private ArrayList<YakindakiMekanlarModel> yakinMekanlar;
    private String markerMekanID= null, markerMekanAd = null;
    YakindakiMekanlarModel secilenmekan;

    ACProgressFlower dialogMekanYüklenmesi, dialogYolTarifiYuklenmesi;

    public NearbyPlacesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fLayout = inflater.inflate(R.layout.fragment_nearby_places, container, false);
        init();
        YakindakiMekanlariGetir();
        return fLayout;
    }

    private void init(){

        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        gps = new GpsBul(getContext());  // constructor a activity mizi yolladık. Orada Context olarak kullanılacak.

        if (gps.canGetLocation()) {  // GPS Lokasyonu alabiliyorsa , kullanıcının anlık konumlarını çektik
            latitude = gps.getLatitude();
            longlatitude = gps.getLongitude();

            lati = Double.toString(latitude);
            longlati = Double.toString(longlatitude);

        } else {  // Değilse Hata Mesajımız ekranda gösterilecek.
            lati = "0";
            longlati = "0";
            Toast.makeText(getContext(), "Lütfen KONUM özelliğini açıp TEKRAR DENEYİNİZ!", Toast.LENGTH_LONG).show();
            //gps.showSettingsAlert();
        }

        fragment_nearby_places_layout = fLayout.findViewById(R.id.fragment_nearby_places_layout);
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

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void haritayıCagir(){
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(fragment_nearby_places_mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longlatitude), 16.5f));

        UiSettings uis = mGoogleMap.getUiSettings();
        uis.setCompassEnabled(true);
        uis.setZoomControlsEnabled(true);
        uis.setMyLocationButtonEnabled(true);

        mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);

        for (int i=0; i<yakinMekanlar.size(); i++){
            addMarker(yakinMekanlar.get(i).getMekanAd(), Double.valueOf(yakinMekanlar.get(i).getLati()), Double.valueOf(yakinMekanlar.get(i).getLonglati()));
        }

    }


    private void addMarker(String title, double latitude, double longitude) {
        MarkerOptions m = new MarkerOptions();
        m.title(title);
        m.draggable(false);
        m.position(new LatLng(latitude, longitude));
        mGoogleMap.addMarker(m);

        mGoogleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        markerMekanAd = marker.getTitle();
         secilenmekan = new YakindakiMekanlarModel();

        for (YakindakiMekanlarModel mekan: yakinMekanlar) {
            if(mekan.getMekanAd().equals(markerMekanAd)) {
                markerMekanID = String.valueOf(mekan.getMekanID());
                secilenmekan = mekan;
            }
        }

        Snackbar snackbar = Snackbar
                .make(fragment_nearby_places_layout, secilenmekan.getMekanAd(), Snackbar.LENGTH_LONG)
                .setAction("Yol Tarifi", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gps = new GpsBul(getContext());
                        if(gps.canGetLocation()) {
                            Intent i = new Intent(getContext(), YolTarifiActivity.class);
                            i.putExtra("hedefLati", Double.valueOf(secilenmekan.getLati()));
                            i.putExtra("hedefLonglati", Double.valueOf(secilenmekan.getLonglati()));
                            startActivity(i);
                        }else {
                            Toast.makeText(getContext(), "Lütfen KONUM özelliğini açıp TEKRAR DENEYİNİZ!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        snackbar.setActionTextColor(Color.YELLOW);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.DKGRAY);
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20);
        snackbar.show();

        return false;
    }

    private void YakindakiMekanlariGetir(){
        dialogMekanYüklenmesi = new ACProgressFlower.Builder(getContext())
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

        service.getYakindakiMekanlar("Sw51QWvmP9-hjk2830ALXzvyT", lati, longlati).enqueue(new Callback<ArrayList<YakindakiMekanlarModel>>() {
            @Override
            public void onResponse(Call<ArrayList<YakindakiMekanlarModel>> call, Response<ArrayList<YakindakiMekanlarModel>> response) {

                if(response.isSuccessful()){
                    yakinMekanlar = new ArrayList<YakindakiMekanlarModel>();
                    yakinMekanlar = response.body();

                    Toast.makeText(getContext(), "Yakınınzda " + yakinMekanlar.size() + " mekan bulundu.", Toast.LENGTH_LONG).show();
                    haritayıCagir();
                    dialogMekanYüklenmesi.dismiss();
                }else {
                    Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
                    dialogYolTarifiYuklenmesi.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<YakindakiMekanlarModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Bir şeyler Ters Gitti", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
