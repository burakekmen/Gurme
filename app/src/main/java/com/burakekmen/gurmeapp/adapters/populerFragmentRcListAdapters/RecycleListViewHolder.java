package com.burakekmen.gurmeapp.adapters.populerFragmentRcListAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.burakekmen.gurmeapp.R;

/****************************
 * Created by Burak EKMEN   |
 * 23.09.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class RecycleListViewHolder extends RecyclerView.ViewHolder{

    public ImageView mekanResmi=null, puanicon=null, viewicon=null;
    public TextView mekanAdi=null, mekanAdresi=null , mekanPuani=null, mekanZiyareti=null;

    public RecycleListViewHolder(View itemView) {
        super(itemView);

        mekanResmi = itemView.findViewById(R.id.cardview_mekanResmi);
        mekanAdi = itemView.findViewById(R.id.cardview_mekanAdi);
        mekanAdresi = itemView.findViewById(R.id.cardview_mekanAdresi);
        mekanPuani = itemView.findViewById(R.id.cardview_mekanPuani);
        mekanZiyareti = itemView.findViewById(R.id.cardview_mekanZiyareti);

        puanicon = itemView.findViewById(R.id.cardview_puanicon);
        viewicon = itemView.findViewById(R.id.cardview_mekanZiyaretIcon);
    }
}
