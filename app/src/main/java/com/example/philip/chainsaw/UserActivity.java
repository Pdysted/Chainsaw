package com.example.philip.chainsaw;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {
    private ImageView userPic;
    private TextView userInfo;

    private String name;
    private int age;
    private String bio;
    private ArrayList<String> photoUrls;

    private int imageIndex = 0;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userPic = (ImageView) findViewById(R.id.userPicsView);
        userInfo = (TextView) findViewById(R.id.userComplInfoView);
        name = getIntent().getStringExtra("NAME");
        age = getIntent().getIntExtra("AGE", 0);
        bio = getIntent().getStringExtra("BIO");
        photoUrls = getIntent().getStringArrayListExtra("PHOTOS");
        Picasso.with(getApplicationContext()).load(photoUrls.get(0)).into(userPic);
        userInfo.setText(name + " " + age + "\n" + bio);
        gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Intent chosenI = new Intent();
                chosenI.putExtra("SELECTED_PIC", photoUrls.get(imageIndex));
                setResult(UserActivity.RESULT_OK, chosenI);
                finish();
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //Swipe right
                if (e1.getX() < e2.getX()) {

                    float xDifference = e2.getX() - e1.getX();
                    float yDifference = (float) 0.0;
                    //If the user is swiping downwards
                    if (e2.getY() > e1.getY()) {
                        yDifference = e2.getY() - e1.getY();

                    }
                    //Check if the user is swiping upwards
                    if (e2.getY() < e1.getY()) {
                        yDifference = e2.getY() - e1.getY();
                    }

                    //Checks if the user is swiping more vertical than horizontal
                    //Make values all positive so difference can be compared
                    xDifference = Math.abs(xDifference);
                    yDifference = Math.abs(yDifference);
                    if (xDifference > yDifference) {
                        if (imageIndex < (photoUrls.size()-1)) {
                            imageIndex++;
                            Picasso.with(getApplicationContext()).load(photoUrls.get(imageIndex)).into(userPic);
                            Log.d("PDBug", "onFlingRight: "+imageIndex);
                        }
                    }
                }

                //Swipe left
                if (e1.getX() > e2.getX()) {
                    float xDifference = e2.getX() - e1.getX();
                    float yDifference = (float) 0.0;
                    //If the user is swiping downwards
                    if (e2.getY() > e1.getY()) {
                        yDifference = e2.getY() - e1.getY();
                    }
                    //Check if the user is swiping upwards
                    if (e2.getY() < e1.getY()) {
                        yDifference = e2.getY() - e1.getY();
                    }

                    //Checks if the user is swiping more vertical than horizontal
                    //Make values all positive so difference can be compared
                    xDifference = Math.abs(xDifference);
                    yDifference = Math.abs(yDifference);
                    if (xDifference > yDifference) {
                            if (imageIndex > 0) {
                                imageIndex--;
                                Picasso.with(getApplicationContext()).load(photoUrls.get(imageIndex)).into(userPic);
                                Log.d("PDBug", "onFlingLeft: " + imageIndex);
                            }
                    }
                }
                return false;
            }
        });
        userPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent chosenI = new Intent();
        chosenI.putExtra("SELECTED_PIC", photoUrls.get(imageIndex));
        setResult(UserActivity.RESULT_OK, chosenI);
        finish();
    }

    //TODO research possibility of showcasing user's instagram pics just as Tinder does
    //TODO research possibilty of integrating spotify music as well
    //TODO dots indicating number of pictures and and currently shown picture atm
    //TODO differ between whether it's a match og rec as Tinder shows button to like or pass if it's a rec
}
