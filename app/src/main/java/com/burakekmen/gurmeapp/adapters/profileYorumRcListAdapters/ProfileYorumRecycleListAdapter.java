package com.burakekmen.gurmeapp.adapters.profileYorumRcListAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.Yorumlar;
import com.burakekmen.gurmeapp.ui.activitys.MekanActivity;

import java.util.ArrayList;

/****************************
 * Created by Burak EKMEN   |
 * 27.09.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class ProfileYorumRecycleListAdapter extends RecyclerView.Adapter<ProfileYorumRecycleListViewHolder> {

    private ArrayList<Yorumlar> yorumListesi = null;
    private Context context = null;

    public ProfileYorumRecycleListAdapter(Context context, ArrayList<Yorumlar> yorumListesi){
        this.context = context;
        this.yorumListesi = yorumListesi;
    }

    @Override
    public ProfileYorumRecycleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_yorum_list_item_cardview, parent, false);
        return new ProfileYorumRecycleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileYorumRecycleListViewHolder holder, int position) {

       final Yorumlar secilenYorum = getItem(position);

        holder.mekanAdi.setText(secilenYorum.getMekanAd());
        holder.yorum.setText(secilenYorum.getYorum());
        holder.tarihi.setText(secilenYorum.getTarihi());

        for(int i=0; i< holder.stars.length; i++){
            if(i < Integer.valueOf(secilenYorum.getPuan()))
                holder.stars[i].setColorFilter((ContextCompat.getColor(context, R.color.yildizSeciliRenk)));
            else
                holder.stars[i].setColorFilter((ContextCompat.getColor(context, R.color.yildizDefaultRenk)));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MekanActivity.class);
                intent.putExtra("mekanID", String.valueOf(secilenYorum.getMekanID()));
                context.startActivity(intent);
            }
        });

        holder.info.setColorFilter((ContextCompat.getColor(context, R.color.facebookrengi)));
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MekanActivity.class);
                intent.putExtra("mekanID", String.valueOf(secilenYorum.getMekanID()));
                context.startActivity(intent);
            }
        });

    }

    private Yorumlar getItem(int position) {

        return yorumListesi.get(position);
    }

    @Override
    public int getItemCount() {
        return yorumListesi.size();
    }
}
