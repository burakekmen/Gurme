package com.burakekmen.gurmeapp.ui.activitys;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.burakekmen.gurmeapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.burakekmen.gurmeapp.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double lati=null, longlati=null;
    private String mekanAd=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Bundle extras = getIntent().getExtras();
        lati = extras.getDouble("lati");
        longlati = extras.getDouble("longlati");
        mekanAd = extras.getString("mekanAd");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    private void addMarker(String title, double latitude, double longitude) {
        MarkerOptions m = new MarkerOptions();
        m.title(title);
        m.draggable(true);
        m.position(new LatLng(latitude, longitude));
        mMap.addMarker(m);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, longlati), 14.5f));

        UiSettings uis = mMap.getUiSettings();
        uis.setCompassEnabled(true);
        uis.setZoomControlsEnabled(true);
        uis.setMyLocationButtonEnabled(true);

        addMarker(mekanAd, lati, longlati);
    }
}
