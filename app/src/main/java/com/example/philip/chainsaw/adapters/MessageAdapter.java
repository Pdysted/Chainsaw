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

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Custom ArrayAdapter for ListView in ChatActivity
 */
public class MessageAdapter extends ArrayAdapter<Message> {
    private final String ownId = "58cb16dd5ac3aa7e03bc6b12";
    private int resource;
    private ArrayList<Message> items;
    private Context context;
    private String photoUrl;

    public MessageAdapter(Context context, int resource, ArrayList<Message> items, String photoUrl) {
        super(context, resource, items);
        this.resource = resource;
        this.items = items;
        this.context = context;
        this.photoUrl = photoUrl;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Message item = getItem(position);
        String messageText = item.getMessageText();
        LinearLayout itemView;

            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            if (item.getSenderId().equals(ownId)) {
                itemView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                li.inflate(resource, itemView, true);
            } else {
                li.inflate(R.layout.message_received_item, itemView, true);
                ImageView chatPic = (ImageView) itemView.findViewById(R.id.chat_picIW);
                Picasso.with(context).load(photoUrl).into(chatPic);
            }

        TextView chatMessage = (TextView) itemView.findViewById(R.id.chatMessageTW);
        chatMessage.setText(messageText);
        return itemView;
    }


}
