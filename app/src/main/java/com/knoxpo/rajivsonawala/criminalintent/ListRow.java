package com.knoxpo.rajivsonawala.criminalintent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

public class ListRow extends RecyclerView.ViewHolder {

    public ImageView mThumbnails;

    public ListRow(View view){

        super(view);

        mThumbnails=(ImageView)view.findViewById(R.id.crime_recycler_view);
    }


}
