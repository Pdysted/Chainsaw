package com.example.philip.chainsaw;


import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.philip.chainsaw.adapters.MessageAdapter;
import com.example.philip.chainsaw.apis.TinderServiceVolley;
import com.example.philip.chainsaw.model.Message;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ImageView headerPic;
    private TextView headerName;
    private EditText messageField;
    private ListView lw;

    private final String ownId = "58cb16dd5ac3aa7e03bc6b12";
    private String tinderToken;
    private String name;
    private String matchId;
    private String photoUrl;
    private ArrayList<Message> messages;
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageField = (EditText) findViewById(R.id.messageFieldET);
        headerName = (TextView) findViewById(R.id.chatMatchNameTW);
        headerPic = (ImageView) findViewById(R.id.chatHeader_picIW);
        tinderToken = getIntent().getStringExtra("TINDER_TOKEN");
        name = getIntent().getStringExtra("NAME");
        matchId = getIntent().getStringExtra("MATCH_ID");
        photoUrl = getIntent().getStringExtra("PHOTO_URL");
        messages = (ArrayList<Message>) getIntent().getSerializableExtra("MESSAGES");
        headerName.setText(name);
        Picasso.with(getApplicationContext()).load(photoUrl).into(headerPic);
        Log.d("PDBug", "onCreate: "+name + " " + photoUrl);
        Log.d("PDBug", "onCreate: "+messages.size());
        lw = (ListView) findViewById(R.id.chatList);
        mAdapter = new MessageAdapter(getApplicationContext(), R.layout.message_item, messages, photoUrl);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                update();
            }
        });
        lw.setAdapter(mAdapter);
        lw.setSelection(mAdapter.getCount()-1);
    }

    public void sendMessage(View v) {
        String text = messageField.getText().toString();
        Message message = new Message(ownId, text, new Date(System.currentTimeMillis()));
        messages.add(message);
        mAdapter.notifyDataSetChanged();
        messageField.setText("");
        try {
            TinderServiceVolley.getInstance(getApplicationContext()).sendMessage(matchId, tinderToken, text);
        } catch (JSONException e) {
            Log.d("PDBug", "sendMessage: "+ e.getLocalizedMessage());
        }
    }

    public void update() {
        lw.setAdapter(mAdapter);
        lw.setSelection(mAdapter.getCount()-1);
    }

    public void backArrow(View v) {
        finish();
    }
}
