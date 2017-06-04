package com.example.philip.chainsaw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philip.chainsaw.apis.TinderServiceVolley;
import com.example.philip.chainsaw.interfaces.CallBack;
import com.example.philip.chainsaw.model.Match;
import com.example.philip.chainsaw.model.Rec;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ImageView profilePic;
    private TextView userInfo;
    private ImageButton messagesButton;

    private int id = 1260393877;
    private SharedPreferences preferences;
    public static final String PREF_FILE_NAME = "savedToken";
    public static final String PREF_TOKEN = "Token";

    String tinderToken;
    ArrayList<Rec> users;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profilePic = (ImageView) findViewById(R.id.userPicView);
        userInfo = (TextView) findViewById(R.id.userInfoView);
        profilePic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        tinderToken = preferences.getString(PREF_TOKEN, "");
        users = null;
        String token = "EAAGm0PX4ZCpsBAJ6YTj1mHg0scLVxIlDFYY9BiusJaUCayRb8gvsWAzgn4ukPuq7tNABZA9hKZC9XNi1HikqZCjizGPG4NFes2fYGxT4JwubZC6qvXjg60FZCOZAZCiswVmplQxZAIsG9VjqsDBI8b4u0kAZCAE5KdZAutSxt3tndvt28DWeNiZBpsOJ0ZAbcbycUFjXJhXGGZCFZAyWq3QoOsN3bZCwWlAUiZBX66QMqMF2J1s5PV93FuqGta8C74KA26b4wB7AZD";
        TinderServiceVolley.getInstance(getApplicationContext()).auth(id, token, new CallBack() {
            @Override
            public void onSuccessAuth(String token) {
                setToken(token);
                Log.d("PDBug", "onSuccessAuth: "+token);
                TinderServiceVolley.getInstance(getApplicationContext()).getRecs(tinderToken, new CallBack() {
                    @Override
                    public void onSuccessAuth(String token) {

                    }

                    @Override
                    public void onSuccessRecs(ArrayList<Rec> tinderUsers) {
                        setUsers(tinderUsers);
                    }

                    @Override
                    public void onSuccessMessages(ArrayList<Match> matches) {

                    }

                    @Override
                    public void onSuccessUser(Match match, String name, String photoUrl) {

                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            }


            @Override
            public void onSuccessRecs(ArrayList<Rec> tinderUsers) {

            }

            @Override
            public void onSuccessMessages(ArrayList<Match> matches) {

            }

            @Override
            public void onSuccessUser(Match match, String name, String photoUrl) {

            }

            @Override
            public void onFail(String msg) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("PDBug", "onCreate: "+tinderToken);

        gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //Swiping right
                Log.d("PDBug", "Swiping");
                if (e1.getX() < e2.getX()) {
                    Log.d("PDBug", "Swiping right");
                    float xDifference = e2.getX() - e1.getX();
                    float yDifference = (float) 0.0;
                    //If the user is swiping downwards
                    if (e2.getY() > e1.getY()) {
                        yDifference = e2.getY() - e1.getY();
                        Log.d("PDBug", "Swiping down");
                    }
                    //Check if the user is swiping upwards
                    if (e2.getY() < e1.getY()) {
                        yDifference = e2.getY() - e1.getY();
                        Log.d("PDBug", "Swiping up");
                    }

                    //Checks if the user is swiping more vertical than horizontal
                    //Make values all positive so difference can be compared
                    xDifference = Math.abs(xDifference);
                    yDifference = Math.abs(yDifference);
                    if (xDifference > yDifference) {
                        if (users.size() > 0) {
                            TinderServiceVolley.getInstance(getApplicationContext()).likeUser(users.get(0).get_id(), tinderToken);
                            users.remove(0);
                            if (users.size() > 1) {
                                Picasso.with(getApplicationContext()).load(users.get(0).getPhotoUrls().get(0)).into(profilePic);
                                userInfo.setText(users.get(0).getName() + "\n" + users.get(0).getBio());
                                Log.d("PDBug", "onFlingNext: " + users.get(0).getName());
                            } else {
                                Log.d("PDBug", "onFling: " + "Reloading");
                                TinderServiceVolley.getInstance(getApplicationContext()).getRecs(tinderToken, new CallBack() {
                                    @Override
                                    public void onSuccessAuth(String token) {

                                    }

                                    @Override
                                    public void onSuccessRecs(ArrayList<Rec> tinderUsers) {
                                        setUsers(tinderUsers);
                                    }

                                    @Override
                                    public void onSuccessMessages(ArrayList<Match> matches) {

                                    }

                                    @Override
                                    public void onSuccessUser(Match match, String name, String photoUrl) {

                                    }

                                    @Override
                                    public void onFail(String msg) {

                                    }
                                });
                            }
                        }
                    }
                }
                //Swiping left
                if (e1.getX() > e2.getX()) {
                    Log.d("PDBug", "Swiping Left");
                    float xDifference = e2.getX() - e1.getX();
                    float yDifference = (float) 0.0;
                    //If the user is swiping downwards
                    if (e2.getY() > e1.getY()) {
                        yDifference = e2.getY() - e1.getY();
                        Log.d("PDBug", "Swiping down");
                    }
                    //Check if the user is swiping upwards
                    if (e2.getY() < e1.getY()) {
                        yDifference = e2.getY() - e1.getY();
                        Log.d("PDBug", "Swiping up");

                    }

                    //Checks if the user is swiping more vertical than horizontal
                    //Make values all positive so difference can be compared
                    xDifference = Math.abs(xDifference);
                    yDifference = Math.abs(yDifference);
                    if (xDifference > yDifference) {
                        if (users.size() > 0) {
                            Log.d("PDBug", "onFling: " + users.get(0).getName());
                            TinderServiceVolley.getInstance(getApplicationContext()).passUser(users.get(0).get_id(), tinderToken);
                            users.remove(0);
                            Picasso.with(getApplicationContext()).load(users.get(0).getPhotoUrls().get(0)).into(profilePic);
                            userInfo.setText(users.get(0).getName() + "\n" + users.get(0).getBio());
                            Log.d("PDBug", "onFlingNext: " + users.get(0).getName());
                            if (users.size() > 1) {
                                Picasso.with(getApplicationContext()).load(users.get(0).getPhotoUrls().get(0)).into(profilePic);
                                userInfo.setText(users.get(0).getName() + "\n" + users.get(0).getBio());
                                Log.d("PDBug", "onFlingNext: " + users.get(0).getName());
                            } else {
                                Log.d("PDBug", "onFling: " + "Reloading");
                                TinderServiceVolley.getInstance(getApplicationContext()).getRecs(tinderToken, new CallBack() {
                                    @Override
                                    public void onSuccessAuth(String token) {

                                    }

                                    @Override
                                    public void onSuccessRecs(ArrayList<Rec> tinderUsers) {
                                        setUsers(tinderUsers);
                                    }

                                    @Override
                                    public void onSuccessMessages(ArrayList<Match> matches) {

                                    }

                                    @Override
                                    public void onSuccessUser(Match match, String name, String photoUrl) {

                                    }

                                    @Override
                                    public void onFail(String msg) {

                                    }
                                });
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    public void setToken(String token) {
        tinderToken = token;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_TOKEN, tinderToken);
        editor.apply();
        Log.d("PDBug", "setToken: " + token);
    }

    public void setUsers(ArrayList<Rec> tinderUsers) {
        users = tinderUsers;
        Rec firstRec = users.get(0);
        Picasso.with(getApplicationContext()).load(users.get(0).getPhotoUrls().get(0)).into(profilePic);
        userInfo.setText(firstRec.getName() + "\n"+firstRec.getBio());
        Log.d("PDBug", "setUsers: "+tinderUsers.size());
    }

    public void goToMessages(View v) {
        Intent i = new Intent(getApplicationContext(), MessagesActivity.class);
        i.putExtra("TINDER_TOKEN", tinderToken);
        startActivity(i);
    }
}
