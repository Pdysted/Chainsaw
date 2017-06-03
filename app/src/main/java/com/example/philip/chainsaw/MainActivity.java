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

import com.example.philip.chainsaw.apis.TinderServiceVolley;
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

        final TinderServiceVolley tsv = new TinderServiceVolley(getApplicationContext());
        String token = "EAAGm0PX4ZCpsBAHJZAKbwl1x2uHy1w2Rq1XCbwZBxkSThCVq0MgeWIZAALypqvdFVOO6jwvXQUBZA2kxRfiZB9efLU50HkMUXqsmQyRnSQNguyiZCefSLwxRjIt5r8BYmIyVptNPJ6sbgGfjEmFo0oXum0HgdZC8oMJXMal0Fc9dzwGouwQLNiN3XlP3uM4MdGG2qHeSZArJP5Y4zcLJQfns2CPBmmTFSoftsINqx2PJ800bPwB9UbeV5UTo2ZC7oFsFgZD";
        tsv.auth(id, token, new TinderServiceVolley.CallBack() {
            @Override
            public void onSuccessAuth(final String token) {
                setToken(token);
                Log.d("PDBug", "onSuccessAuth: "+token);
                tsv.getRecs(tinderToken, new TinderServiceVolley.CallBack() {
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

            }
        });
        Log.d("PDBug", "onCreate: "+tinderToken);
        tsv.getRecs(tinderToken, new TinderServiceVolley.CallBack() {
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
                                tsv.likeUser(users.get(0).get_id(), tinderToken);
                                users.remove(0);
                                if (users.size() > 1) {
                                    Picasso.with(getApplicationContext()).load(users.get(0).getPhotoUrls().get(0)).into(profilePic);
                                    userInfo.setText(users.get(0).getName() + "\n" + users.get(0).getBio());
                                    Log.d("PDBug", "onFlingNext: " + users.get(0).getName());
                                } else {
                                    Log.d("PDBug", "onFling: " + "Reloading");
                                    tsv.getRecs(tinderToken, new TinderServiceVolley.CallBack() {
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
                            tsv.passUser(users.get(0).get_id(), tinderToken);
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
                                tsv.getRecs(tinderToken, new TinderServiceVolley.CallBack() {
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
        Rec d2 = users.get(0);
        Picasso.with(getApplicationContext()).load(users.get(0).getPhotoUrls().get(0)).into(profilePic);
        userInfo.setText(d2.getName() + "\n"+d2.getBio());
        Log.d("PDBug3", "setUsers: "+tinderUsers.size());
        TinderServiceVolley tsv = new TinderServiceVolley(getApplicationContext());
        tsv.likeUser(d2.get_id(), tinderToken);
    }

    public void goToMessages(View v) {
        Intent i = new Intent(getApplicationContext(), MessagesActivity.class);
        i.putExtra("TINDER_TOKEN", tinderToken);
        startActivity(i);
    }
}
