package com.burakekmen.gurmeapp.adapters.profileYorumRcListAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.burakekmen.gurmeapp.R;

/****************************
 * Created by Burak EKMEN   |
 * 27.09.2017               |
 * ekmen.burak@hotmail.com  |
 ***************************/

public class ProfileYorumRecycleListViewHolder extends RecyclerView.ViewHolder{

    public TextView mekanAdi=null, yorum=null, tarihi=null;
    public ImageView [] stars;
    public ImageView info;


    public ProfileYorumRecycleListViewHolder(View itemView) {
        super(itemView);

        mekanAdi = itemView.findViewById(R.id.profile_yorum_list_item_MekanAdi);
        yorum = itemView.findViewById(R.id.profile_yorum_list_item_yorum);
        tarihi = itemView.findViewById(R.id.profile_yorum_list_item_txtTarihi);

        ImageView starOne = itemView.findViewById(R.id.profile_yorum_list_item_starOne);
        ImageView starTwo = itemView.findViewById(R.id.profile_yorum_list_item_starTwo);
        ImageView starThree = itemView.findViewById(R.id.profile_yorum_list_item_starThree);
        ImageView starFour = itemView.findViewById(R.id.profile_yorum_list_item_starFour);
        ImageView starFive = itemView.findViewById(R.id.profile_yorum_list_item_starFive);

        stars = new ImageView[]{starOne, starTwo, starThree, starFour, starFive};

        info = itemView.findViewById(R.id.profile_yorum_list_item_info);
    }
}
