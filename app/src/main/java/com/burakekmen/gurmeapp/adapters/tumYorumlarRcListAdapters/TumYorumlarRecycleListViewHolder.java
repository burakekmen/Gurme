package com.burakekmen.gurmeapp.adapters.tumYorumlarRcListAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.burakekmen.gurmeapp.R;

/****************************
 * Created by Burak EKMEN   |
 * 6.10.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class TumYorumlarRecycleListViewHolder extends RecyclerView.ViewHolder{

    public TextView kullaniciAdi=null, yorum=null, tarihi=null;
    public ImageView [] stars;


    public TumYorumlarRecycleListViewHolder(View itemView) {
        super(itemView);

        kullaniciAdi = itemView.findViewById(R.id.yorum_list_item_kullaniciAdi);
        yorum = itemView.findViewById(R.id.yorum_list_item_yorum);
        tarihi = itemView.findViewById(R.id.mekan_yorum_list_item_txtTarihi);

        ImageView starOne = itemView.findViewById(R.id.mekan_yorum_list_item_starOne);
        ImageView starTwo = itemView.findViewById(R.id.mekan_yorum_list_item_starTwo);
        ImageView starThree = itemView.findViewById(R.id.mekan_yorum_list_item_starThree);
        ImageView starFour = itemView.findViewById(R.id.mekan_yorum_list_item_starFour);
        ImageView starFive = itemView.findViewById(R.id.mekan_yorum_list_item_starFive);

        stars = new ImageView[]{starOne, starTwo, starThree, starFour, starFive};
    }
}
