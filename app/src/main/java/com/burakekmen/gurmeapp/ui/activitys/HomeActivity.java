package com.burakekmen.gurmeapp.ui.activitys;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.PopulerMekanModel;
import com.burakekmen.gurmeapp.ui.fragments.FavoritesFragment;
import com.burakekmen.gurmeapp.ui.fragments.LoginFragment;
import com.burakekmen.gurmeapp.ui.fragments.NearbyPlacesFragment;
import com.burakekmen.gurmeapp.ui.fragments.PopulerFragment;
import com.burakekmen.gurmeapp.ui.fragments.ProfileFragment;
import com.burakekmen.gurmeapp.ui.fragments.SearchFragment;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private BottomNavigationView bottomNavigation;
    private Fragment fragment, PopulerFragment, SearchFragment, NearbyPlacesFragment, FavoritesFragment, ProfileFragment, LoginFragment;
    private FragmentManager fragmentManager;
    private ArrayList<PopulerMekanModel> mekanListesi = null;

    private RelativeLayout homeActivityLayout;

    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        acilisAyarlariniYap();
        init();

    }

    private void acilisAyarlariniYap(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_logo);

        final SharedPreferences pref = getApplicationContext().getSharedPreferences("session", MODE_PRIVATE);
        if (!pref.getBoolean("isLogged", false)) {

            SharedPreferences.Editor edit = pref.edit();
            edit.putBoolean("isLogged", false);
            edit.putString("mail", "");
            edit.putString("sifre", "");

            edit.apply();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (pref.getBoolean("ilkAcilisMi", true)) { // TODO: İLK AÇILIŞ TRUE İSE

            Fragment populerFragment = new PopulerFragment();

            Bundle extras = getIntent().getExtras();
            mekanListesi = extras.getParcelableArrayList("mekanListesi");

            if(mekanListesi != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("mekanListesi", mekanListesi);
                populerFragment.setArguments(bundle);
            }

            fragmentTransaction.add(R.id.activity_home_fragment_container, populerFragment);
            fragmentTransaction.commit();
        }else {
            Fragment populerFragment = new PopulerFragment();
            fragmentTransaction.add(R.id.activity_home_fragment_container, populerFragment);
            fragmentTransaction.commit();
        }

    }

    public void init(){
        homeActivityLayout =  findViewById(R.id.activity_home_layout);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_populer:
                if(PopulerFragment == null)
                    PopulerFragment = new PopulerFragment();

                fragment = PopulerFragment;
                break;
            case R.id.action_search:
                if(SearchFragment == null)
                    SearchFragment = new SearchFragment();

                fragment = SearchFragment;
                break;
            case R.id.action_nearPlaces:
                if (NearbyPlacesFragment == null)
                    NearbyPlacesFragment = new NearbyPlacesFragment();

                fragment = NearbyPlacesFragment;
                break;
            case R.id.action_favori:
                if(FavoritesFragment == null)
                    FavoritesFragment = new FavoritesFragment();

                fragment = FavoritesFragment;
                break;
            case R.id.action_profil:
                SharedPreferences pref = this.getSharedPreferences("session",MODE_PRIVATE);

                if(pref.getBoolean("isLogged",false)) {
                    if (ProfileFragment == null)
                        ProfileFragment = new ProfileFragment();

                    fragment = ProfileFragment;
                }
                else{
                    if (LoginFragment == null)
                        LoginFragment = new LoginFragment();

                    fragment = LoginFragment;
                }
                break;
            default:
                break;
        }

        if(isOnline()) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.activity_home_fragment_container, fragment).commit();
            getFragmentManager().popBackStack();
            return true;
        }else {
            Snackbar snackbar1 = Snackbar.make(homeActivityLayout, "İnternet Bağlantınızı Kontrol Ediniz!", Snackbar.LENGTH_SHORT);
            snackbar1.show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        //Checking for fragment count on backstack
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;

            Snackbar snackbar1 = Snackbar.make(homeActivityLayout, "Çıkış için tekrar Geri tuşuna basınız!", Snackbar.LENGTH_SHORT);
            snackbar1.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }




}
