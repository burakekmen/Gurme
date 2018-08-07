package com.burakekmen.gurmeapp.adapters.populerFragmentRcListAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.PopulerMekanModel;
import com.burakekmen.gurmeapp.ui.activitys.MekanActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/****************************
 * Created by Burak EKMEN   |
 * 23.09.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class RecycleListAdapter extends RecyclerView.Adapter<RecycleListViewHolder> {

    private ArrayList<PopulerMekanModel> mekanListesi = null;
    private Context context = null;

    public RecycleListAdapter(Context context, ArrayList<PopulerMekanModel> mekanListesi) {
        this.context = context;
        this.mekanListesi = mekanListesi;
    }

    @Override
    public RecycleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_item_cardview, parent, false);
        return new RecycleListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecycleListViewHolder holder, int position) {
        final PopulerMekanModel secilenMekan = getItem(position);

        String puan;
        if(secilenMekan.getPuan().equals("-1"))
            puan = "Oy Yok";
        else
            puan = secilenMekan.getPuan();

        Picasso.with(context).load(secilenMekan.getKapakResmi()).into(holder.mekanResmi);
        holder.mekanAdi.setText(secilenMekan.getMekanAd());
        holder.mekanAdresi.setText(secilenMekan.getAdresi());
        holder.mekanPuani.setText(puan);
        holder.mekanZiyareti.setText(secilenMekan.getZiyaret());
        holder.puanicon.setColorFilter((ContextCompat.getColor(context, R.color.yildizSeciliRenk)));
        holder.viewicon.setColorFilter((ContextCompat.getColor(context, R.color.facebookrengi)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MekanActivity.class);
                intent.putExtra("mekanID", String.valueOf(secilenMekan.getMekanID()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mekanListesi.size();
    }

    private PopulerMekanModel getItem(int position) {

        return mekanListesi.get(position);
    }



}
