package com.example.philip.chainsaw;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private String name;
    private String photoUrl;
    private ArrayList<Message> messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        name = getIntent().getStringExtra("NAME");
        photoUrl = getIntent().getStringExtra("PHOTO_URL");
        messages = (ArrayList<Message>) getIntent().getSerializableExtra("MESSAGES");
        Log.d("PDBug", "onCreate: "+name + " " + photoUrl + messages.size());

    }
}
