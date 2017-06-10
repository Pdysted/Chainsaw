package com.example.philip.chainsaw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UserActivity extends AppCompatActivity {
    private ImageView userPic;
    private TextView userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userPic = (ImageView) findViewById(R.id.userPicsView);
        userInfo = (TextView) findViewById(R.id.userComplInfoView);
    }

    //TODO research possibility of showcasing user's instagram pics just as Tinder does
    //TODO research possibilty of integrating spotify music as well
    //TODO dots indicating number of pictures and and currently shown picture atm
    //TODO differ between whether it's a match og rec as Tinder shows button to like or pass if it's a rec
}
