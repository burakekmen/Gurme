package com.burakekmen.gurmeapp.adapters.mekanActivityRcListAdapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burakekmen.gurmeapp.R;
import com.burakekmen.gurmeapp.network.retrofit.gsonModels.UyeYorum;

import java.util.ArrayList;

/****************************
 * Created by Burak EKMEN   |
 * 27.09.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class YorumRecycleListAdapter extends RecyclerView.Adapter<YorumRecycleListViewHolder> {

    private ArrayList<UyeYorum> yorumListesi = null;
    private Context context = null;

    public YorumRecycleListAdapter(Context context, ArrayList<UyeYorum> yorumListesi){
        this.context = context;
        this.yorumListesi = yorumListesi;
    }

    @Override
    public YorumRecycleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mekan_yorum_list_item, parent, false);
        return new YorumRecycleListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(YorumRecycleListViewHolder holder, int position) {

       final UyeYorum secilenYorum = getItem(position);

        holder.kullaniciAdi.setText(secilenYorum.getKullaniciAdi());
        holder.yorum.setText(secilenYorum.getYorum());
        holder.tarihi.setText(secilenYorum.getTarihi());

        for (int i = 0; i < holder.stars.length; i++) {
            if (i < Integer.valueOf(secilenYorum.getPuan()))
                holder.stars[i].setColorFilter((ContextCompat.getColor(context, R.color.yildizSeciliRenk)));
            else
                holder.stars[i].setColorFilter((ContextCompat.getColor(context, R.color.yildizDefaultRenk)));
        }

    }

    private UyeYorum getItem(int position) {

        return yorumListesi.get(position);
    }

    @Override
    public int getItemCount() {
        return yorumListesi.size();
    }
}
