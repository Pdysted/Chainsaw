package com.example.philip.chainsaw.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.philip.chainsaw.R;
import com.example.philip.chainsaw.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for ListView in ChatActivity
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private int resource;
    private ArrayList<Message> items;
    private Context context;

    public MessageAdapter(Context context, int resource, ArrayList<Message> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
        this.context = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Message item = getItem(position);
        String messageText = item.getMessageText();
        LinearLayout itemView;
        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        /*if (picUrl.equals("")) {
        } else {
            Picasso.with(context).load(item.getPhotoUrl()).into(picView);
        }*/
        return itemView;
    }


}
