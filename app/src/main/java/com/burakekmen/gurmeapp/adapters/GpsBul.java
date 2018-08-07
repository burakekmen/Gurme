package com.burakekmen.gurmeapp.adapters;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

/**
 * Created by Burak on 7.04.2017.
 */

public class GpsBul extends Service implements LocationListener {

    private final Context context;

    boolean isGPSEnabled = false;
    boolean isNetworkEnable = false;
    boolean canGetLocation = false;

    double latitude, longlatitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10; //metre
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; //saniye

    protected LocationManager locationManager;

    Location location;

    // Constructora atamada bulunduk.
    public GpsBul(Context context) {
        this.context = context;
        getLocation();
    }

    // konum alma islemini yaptigimiz kisim burasi
    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnable) {

            } else { //eÄŸer gps ve network enable ise
                this.canGetLocation = true;

                if (isNetworkEnable) {

                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATE,
                            this);


                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longlatitude = location.getLongitude();
                        }
                    }


                }

                if (isGPSEnabled) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        }
                        locationManager.requestLocationUpdates(LocationManager.
                                        GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATE,
                                this);
                        if (locationManager == null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longlatitude = location.getLongitude();
                            }

                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GpsBul.this);
        }
    }

    // enlem degerini alacagimiz fonksiyon
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    // boylam degerini alacagimiz fonksiyon
    public double getLongitude(){
        if(location != null){
            longlatitude = location.getLongitude();
        }
        return  longlatitude;
    }

    // eÄŸer location alinabiliyorsa true donuyor
    public  boolean canGetLocation(){
        return  this.canGetLocation;
    }

    //eger gps kapaliysa gosterecegimiz mesajlar
    public  void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        alertDialog.setTitle("GPS Ayarla");
        alertDialog.setMessage("GPS aktif değil. Ayarlar penceresine gitmek ister misiniz?");

        //AlertDialog penceresinde Evet e bastiginda Ayarlar a gidiyor
        alertDialog.setPositiveButton("Ayarlar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        //tam tersi
        alertDialog.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        getLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}