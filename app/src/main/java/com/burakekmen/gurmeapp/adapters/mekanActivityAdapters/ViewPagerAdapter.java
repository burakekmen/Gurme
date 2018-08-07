package com.burakekmen.gurmeapp.adapters.mekanActivityAdapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.MekanResimsUrl;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/****************************
 * Created by Burak EKMEN   |
 * 25.09.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<MekanResimsUrl> resimListesi;

    public ViewPagerAdapter(Context context, ArrayList<MekanResimsUrl> resimListesi) {
        this.context = context;
        this.resimListesi = resimListesi;
    }


    @Override
    public int getCount() {
        return resimListesi.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        MekanResimsUrl resimUrl = getItem(position);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, null);
        ImageView resim = view.findViewById(R.id.slider_layout_resim);

        Picasso.with(context).load(resimListesi.get(position).getResimUrl()).into(resim);

        resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resmiGoruntule(position);
            }
        });

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;

        viewPager.removeView(view);
    }


    private MekanResimsUrl getItem(int position) {
        MekanResimsUrl resim = null;
        for (int i = 0; i < resimListesi.size(); i++) {
            if (i == position)
                resim = resimListesi.get(i);
        }

        return resim;
    }

    private void resmiGoruntule(int position){
        final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setCancelable(false);
        nagDialog.setContentView(R.layout.preview_image);
        PhotoView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);

        nagDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    nagDialog.dismiss();
                }
                return true;
            }
        });

        Window window = nagDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        window.setBackgroundDrawableResource(R.color.secondary_text);

        Picasso.with(context).load(resimListesi.get(position).getResimUrl()).into(ivPreview);
        nagDialog.show();
    }


}