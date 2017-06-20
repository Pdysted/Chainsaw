package com.example.philip.chainsaw;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.StackView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philip.chainsaw.adapters.RecStackAdapter;
import com.example.philip.chainsaw.apis.TinderServiceVolley;
import com.example.philip.chainsaw.interfaces.CallBack;
import com.example.philip.chainsaw.model.Rec;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import link.fls.swipestack.SwipeStack;

public class MainActivity extends AppCompatActivity {
    public static final String PREF_FILE_NAME = "savedToken";
    public static final String PREF_TOKEN = "Token";
    public static final int USER_ACTIVITY = 3;
    private final int id = 1260393877;

    private SwipeStack profileStack;
    private GestureDetector gestureDetector;
    private ProgressBar recsProgressBar;
    private TextView recsProgressText;


    private SharedPreferences preferences;

    private String tinderToken;
    private ArrayList<Rec> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recsProgressBar = (ProgressBar) findViewById(R.id.progressBarRecs);
        recsProgressText = (TextView) findViewById(R.id.progressTextRecs);
        preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        tinderToken = preferences.getString(PREF_TOKEN, "");
        users = new ArrayList<>();
        String token = "EAAGm0PX4ZCpsBAHvkE3PZBIa86cztaJPQOED64eYGutB9kmD1ZB1RCMZAkQEW0bNMTJ1fz3ZAB2KIdFZAUtrc0i4Kxw7AcSyTO5zrFMLerI1mfNbEWOqDcvjG57pXFFVRCNYE4V5p5Rc1eQH8BWoUAO4YJorGn3oKMFFS3trpJeHq2AhIiB4YbBS6DdcU8ZBnNzt6P1qTQNCZBAMR2KS7jk705BhzYZASLLfSk75C4ABtwemeiPF7z3phOYohKVQeQ1EZD";
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
                        recsProgressBar.setVisibility(View.GONE);
                        recsProgressText.setVisibility(View.GONE);
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
                //Try with the previous acquired token from tinder in the database
                TinderServiceVolley.getInstance(getApplicationContext()).getRecs(tinderToken, new CallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        recsProgressBar.setVisibility(View.GONE);
                        recsProgressText.setVisibility(View.GONE);
                        addUsers(response);
                    }

                    @Override
                    public void onFail(String msg) {
                        Toast.makeText(getApplicationContext(), "Tinder authentication token expired", Toast.LENGTH_LONG);
                    }
                });
            }
        });
        profileStack = (SwipeStack) findViewById(R.id.profileStackView);
        profileStack.setZ(-1);
        //Unable to add a onTouch listener as the swipe listener intercepts the check
        //GitHub page is working on the issue
        profileStack.setListener(new SwipeStack.SwipeStackListener() {
            @Override
            public void onViewSwipedToLeft(int position) {
                Log.d("PDBug", "onViewSwipedToLeft: ");
                TinderServiceVolley.getInstance(getApplicationContext()).passUser(users.get(0).get_id(), tinderToken);
                users.remove(0);
            }

            @Override
            public void onViewSwipedToRight(int position) {
                if (users.size() > 0) {
                    Log.d("PDBug", "onViewSwipedToRight: ");
                    TinderServiceVolley.getInstance(getApplicationContext()).likeUser(users.get(0).get_id(), tinderToken);
                    users.remove(0);
                    }
                }

            @Override
            public void onStackEmpty() {
                Log.d("PDBug", "onStackEmpty: " + "Reloading");
                TinderServiceVolley.getInstance(getApplicationContext()).getRecs(tinderToken, new CallBack() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        recsProgressBar.setVisibility(View.GONE);
                        recsProgressText.setVisibility(View.GONE);
                        addUsers(response);
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });
                recsProgressBar.setVisibility(View.VISIBLE);
                recsProgressText.setVisibility(View.VISIBLE);
            }
        });


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
                }
                Date birthDay = df.parse(jsonObj.getString("birth_date"));
                String bio = "";
                if (jsonObj.has("bio")) {
                    bio = jsonObj.getString("bio");
                }
                Rec user = new Rec(jsonObj.getInt("distance_mi"), jsonObj.getString("_id"), bio,
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
        RecStackAdapter recAdapt = new RecStackAdapter(getApplicationContext(), R.layout.rec_stack_item, users);
        profileStack.setAdapter(recAdapt);
        recAdapt.notifyDataSetChanged();
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
            }
        }
    }


}
