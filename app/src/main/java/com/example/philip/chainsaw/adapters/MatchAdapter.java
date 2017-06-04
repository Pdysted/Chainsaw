package com.example.philip.chainsaw.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.philip.chainsaw.R;
import com.example.philip.chainsaw.model.Match;
import com.squareup.picasso.Picasso;


import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Custom ArrayAdapter for ListView in MessagesActivity
 */
public class MatchAdapter extends ArrayAdapter<Match> {
    private int resource;
    private ArrayList<Match> items;
    private Context context;

    public MatchAdapter(Context context, int resource, ArrayList<Match> items) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
        this.context = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Match item = getItem(position);
        String message = "No messages";
        if (item.getMessages().size() > 0) {
            message = item.getMessages().get((item.getMessages().size()-1)).getMessageText();
        }
        LinearLayout itemView;
        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            //Log.d("PDBug", "getView: " + resource);
            li.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }
        TextView nameView = (TextView) itemView.findViewById(R.id.message_item_nameTW);
        TextView messageView = (TextView) itemView.findViewById(R.id.message_item_messageTW);
        ImageView picView = (ImageView) itemView.findViewById(R.id.message_picIW);
        nameView.setText(item.getName());
        messageView.setText(message);
        String picUrl = item.getPhotoUrl();
        if (picUrl.equals("")) {
        } else {
            Picasso.with(context).load(item.getPhotoUrl()).into(picView);
        }
        return itemView;
    }


}
