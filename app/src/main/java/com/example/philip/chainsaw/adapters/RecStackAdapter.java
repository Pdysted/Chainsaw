package com.example.philip.chainsaw.adapters;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.philip.chainsaw.R;
import com.example.philip.chainsaw.model.Rec;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by philip on 6/13/17.
 */

public class RecStackAdapter extends ArrayAdapter<Rec> {
    private Context context;
    private int resource;
    private List<Rec> items;


    public RecStackAdapter(Context context, int resource, List<Rec> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Rec getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("PDBug", "getView: "+"getView");
        Rec item = getItem(position);
        LinearLayout itemView;
        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            Log.d("PDBug", "getViewRec: " + resource);
            li.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        TextView userInfo = (TextView) itemView.findViewById(R.id.userSInfoView);
        ImageView picView = (ImageView) itemView.findViewById(R.id.userSPicView);
        userInfo.setText(item.getName() + " " + item.getAge());
        String picUrl = item.getPhotoUrls().get(0);
        if (picUrl.equals("")) {
        } else {
            Picasso.with(context).load(picUrl).transform(new RoundedCornersTransformation(10, 10)).into(picView);
        }
        return itemView;
    }


}
