package com.example.philip.chainsaw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philip.chainsaw.apis.TinderServiceVolley;
import com.example.philip.chainsaw.interfaces.CallBack;
import com.example.philip.chainsaw.model.Match;
import com.example.philip.chainsaw.model.Rec;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MainActivity extends AppCompatActivity {
    private ImageView profilePic;
    private TextView userInfo;
    private ImageButton messagesButton;
    private LinearLayout profileLayout;

    private int id = 1260393877;
    private SharedPreferences preferences;
    public static final String PREF_FILE_NAME = "savedToken";
    public static final String PREF_TOKEN = "Token";
    public static final int USER_ACTIVITY = 3;

    private float layoutX;
    private float layoutY;
    float dX, dY;
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
        users = new ArrayList<>();
        String token = "EAAGm0PX4ZCpsBADo2dr0R7j2O6pIZAYY7NS7xayTGfBZCw22ZByQldpvtuXxtY2zaqnffonsgUWorZBB1XuqE8PGCvMx2HohmIYURIBgBy66FzhLgDKUgiAn5ksr8aIYMbEngWZCnQsNwOLCXQplc4OUDjg4rGnZACFIh9ubZBYsXR8mRt46IHJTw98xkKhGvwm8R33Y4yxqjcbkZBkZCehrXANh1g3XfMdXaUWYDTqb4KFucpuIeWtyt4lklRtZBzZCrdIZD";
        TinderServiceVolley.getInstance(getApplicationContext()).auth(id, token, new CallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                String token = null;
                try {
                    token = response.getString("token");
                    setToken(token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("PDBug", "onSuccessAuth: "+token);
                TinderServiceVolley.getInstance(getApplicationContext()).getRecs(tinderToken, new CallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                            addUsers(response);
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
            }

            @Override
            public void onFail(String msg) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
        profileLayout = (LinearLayout) findViewById(R.id.ss);
        layoutX = profileLayout.getX();
        layoutY = profileLayout.getY();
        profileLayout.setZ(-1);
        //TODO using a stacked stackview and having multiple layouts stacked would allow to "remove" it instead of the animation going back
        profileLayout.setOnTouchListener(new View.OnTouchListener() {
                                      @Override
                                      public boolean onTouch(View view, MotionEvent event) {

                                          switch (event.getAction()) {


                                              case MotionEvent.ACTION_DOWN:
                                                  dX = view.getX() - event.getRawX();
                                                  break;

                                              case MotionEvent.ACTION_MOVE:

                                                  view.animate()
                                                          .x(event.getRawX() + dX)
                                                          .setDuration(0)
                                                          .start();

                                                  int viewX = (int)view.getX();
                                                  if (viewX > 240) {
                                                      Log.d("PDBug", "onTouch: right");
                                                      view.animate().x(layoutX+35).setDuration(100).start();
                                                      if (users.size() > 0) {
                                                          TinderServiceVolley.getInstance(getApplicationContext()).likeUser(users.get(0).get_id(), tinderToken);
                                                          users.remove(0);
                                                          if (users.size() > 1) {
                                                              setUsers();
                                                              Log.d("PDBug", "onFlingNext: " + users.get(0).getName());
                                                          } else {
                                                              Log.d("PDBug", "onFling: " + "Reloading");
                                                              TinderServiceVolley.getInstance(getApplicationContext()).getRecs(tinderToken, new CallBack() {
                                                                  @Override
                                                                  public void onSuccess(JSONObject response) {
                                                                      addUsers(response);
                                                                  }

                                                                  @Override
                                                                  public void onFail(String msg) {

                                                                  }
                                                              });
                                                          }
                                                      }
                                                  }
                                                  if (viewX < -240) {
                                                      Log.d("PDBug", "onTouch: left");
                                                      view.animate().x(layoutX+35).setDuration(100).start();
                                                      if (users.size() > 0) {
                                                          Log.d("PDBug", "onFling: " + users.get(0).getName());
                                                          TinderServiceVolley.getInstance(getApplicationContext()).passUser(users.get(0).get_id(), tinderToken);
                                                          users.remove(0);
                                                          if (users.size() > 1) {
                                                              setUsers();
                                                              Log.d("PDBug", "onFlingNext: " + users.get(0).getName());
                                                          } else {
                                                              Log.d("PDBug", "onFling: " + "Reloading");
                                                              TinderServiceVolley.getInstance(getApplicationContext()).getRecs(tinderToken, new CallBack() {
                                                                  @Override
                                                                  public void onSuccess(JSONObject response) {
                                                                      addUsers(response);
                                                                  }

                                                                  @Override
                                                                  public void onFail(String msg) {

                                                                  }
                                                              });
                                                          }
                                                      }
                                                  }
                                                  break;
                                              case MotionEvent.ACTION_UP:
                                                  //view.animate().x(layoutX+35).setDuration(100).start();
                                                  break;
                                              default:
                                                  view.animate().x(layoutX+35).setDuration(100).start();
                                                  return false;
                                          }
                                          return true;
                                      }

        } );

        Log.d("PDBug", "onCreate: "+tinderToken);

        gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Intent i = new Intent(getApplicationContext(), UserActivity.class);
                i.putExtra("NAME", users.get(0).getName());
                i.putExtra("AGE", users.get(0).getAge());
                i.putExtra("BIO", users.get(0).getBio());
                i.putExtra("PHOTOS", users.get(0).getPhotoUrls());
                startActivityForResult(i, USER_ACTIVITY);
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

    public void addUsers(JSONObject jsonResponse) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            JSONArray jsonUsers = jsonResponse.getJSONArray("results");
            for (int i = 0; i < jsonUsers.length(); i++) {
                JSONObject jsonObj = jsonUsers.getJSONObject(i);
                ArrayList<String> photosUrls = new ArrayList<>();
                JSONArray photosArray = jsonObj.getJSONArray("photos");
                for (int j = 0; j < photosArray.length(); j++) {
                    JSONObject photoJson = photosArray.getJSONObject(j);
                    photosUrls.add(photoJson.getString("url"));
                    //Log.d("PDBug", "photoArray: " + photoJson.getString("url"));
                }
                Log.d("PDBug", "addUsers: "+jsonObj.getString("birth_date"));
                Date birthDay = df.parse(jsonObj.getString("birth_date"));
                Rec user = new Rec(jsonObj.getInt("distance_mi"), jsonObj.getString("_id"), jsonObj.getString("bio"),
                        birthDay, jsonObj.getInt("gender"), jsonObj.getString("name"), photosUrls);
                users.add(user);
            }
            setUsers();
        } catch (ParseException ex) {
              Log.d("PDBug", "addUsersParse: "+ex.getLocalizedMessage());
        } catch(JSONException ex) {
            Log.d("PDBug", "addUsersJSON: "+ex.getLocalizedMessage());
        }
    }

    public void setUsers() {
        Rec firstRec = users.get(0);
        Picasso.with(getApplicationContext()).load(firstRec.getPhotoUrls().get(0)).transform(new RoundedCornersTransformation(10, 10)).into(profilePic);
        userInfo.setText(firstRec.getName() + " " + firstRec.getAge());
        firstRec.getAge();
        Log.d("PDBug", "setUsers: "+users.size());
    }

    public void goToMessages(View v) {
        Intent i = new Intent(getApplicationContext(), MessagesActivity.class);
        i.putExtra("TINDER_TOKEN", tinderToken);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == USER_ACTIVITY) {
            if (resultCode == UserActivity.RESULT_OK) {
                String selectedPic = data.getStringExtra("SELECTED_PIC");
                Log.d("PDBug", "onActivityResult: "+selectedPic);
                Picasso.with(getApplicationContext()).load(selectedPic).transform(new RoundedCornersTransformation(10, 10)).into(profilePic);
            }
        }
    }


}
