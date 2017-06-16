package com.example.philip.chainsaw;

import android.content.Intent;
import android.database.DataSetObserver;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.philip.chainsaw.adapters.MatchAdapter;
import com.example.philip.chainsaw.apis.TinderServiceRetrofit;
import com.example.philip.chainsaw.apis.TinderServiceVolley;
import com.example.philip.chainsaw.interfaces.CallBack;
import com.example.philip.chainsaw.model.Match;
import com.example.philip.chainsaw.model.Message;
import com.example.philip.chainsaw.model.Rec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MessagesActivity extends AppCompatActivity {
    private EditText searchField;
    private ListView lw;

    private String tinderToken;
    MatchAdapter mAdapter;
    private ArrayList<Match> matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        lw = (ListView) findViewById(R.id.messageList);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Match m = (Match) parent.getAdapter().getItem(position);
                Log.d("PDBug", "onItemClick: "+ m.getName());
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                i.putExtra("TINDER_TOKEN", tinderToken);
                i.putExtra("NAME", m.getName());
                i.putExtra("MATCH_ID", m.getUserId());
                i.putExtra("PHOTO_URL", m.getPhotoUrl());
                i.putExtra("MESSAGES", m.getMessages());
                startActivity(i);
            }
        });
        tinderToken = getIntent().getStringExtra("TINDER_TOKEN");
        matches = new ArrayList<>();
        TinderServiceVolley.getInstance(getApplicationContext()).getMessages(tinderToken, new CallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                    addMatches(response);
            }

            @Override
            public void onFail(String msg) {

            }
        });
        searchField = (EditText) findViewById(R.id.search_field);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchMatches(s.toString());
            }
        });

        /*Retrofit builder = new Retrofit.Builder().baseUrl("https://api.gotinder.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        TinderServiceRetrofit tsr = builder.create(TinderServiceRetrofit.class);

        Call<List<Match>> matches = tsr.getMessages("octocat");
        matches.enqueue(new Callback<List<Match>>() {
            @Override
            public void onResponse(Call<List<Match>> call, Response<List<Match>> response) {

            }

            @Override
            public void onFailure(Call<List<Match>> call, Throwable t) {

            }
        });*/


    }

    public void addMatches(JSONObject response) {
        try {
            JSONArray matchesJson = response.getJSONArray("matches");
            for (int i = 0; i < matchesJson.length(); i++) {
                JSONArray messagesJson = matchesJson.getJSONObject(i).getJSONArray("messages");
                ArrayList<Message> messages = new ArrayList<>();
                for (int j = 0; j < messagesJson.length(); j++) {
                    //Build messages here
                    JSONObject messageJson = messagesJson.getJSONObject(j);
                    Date date = new Date(messageJson.getLong("timestamp"));

                    Message message = new Message(messageJson.getString("from"), messageJson.getString("message"), date);
                    messages.add(message);
                }
                //Matches are sorted depending on who likes first
                //Check for my id within the first 24 characters and substring accordingly
                String matchId = matchesJson.getJSONObject(i).getString("_id");
                if (matchId.substring(0, 24).equals("58cb16dd5ac3aa7e03bc6b12")) {
                    matchId = matchId.substring(24, 48);
                } else {
                    matchId = matchId.substring(0, 24);
                }
                Match match = new Match(matchId, messages);
                matches.add(match);
                //Log.d("PDBug", "onResponseUserID: "+match.getUserId() + " length" + match.getUserId().length());
                //Scalable only 10 at a time and dynamically add to the adapter?
                //Slow loading?
                }
            getUsers();
            }   catch (JSONException ex) {
            Log.d("PDBug", "getMatches: "+ex.getLocalizedMessage());
        }
    }

    public void getUsers() {
        for (int i = 0; i < matches.size(); i++) {
            Log.d("PDBus", "getUsers: "+matches.size());
            final int iterator = i;
            TinderServiceVolley.getInstance(getApplicationContext()).getUser(matches.get(i), tinderToken, new CallBack() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        //Log.d("PDBug", "onResponseGetUser: " + response.toString());
                        JSONObject results = response.getJSONObject("results");
                        String name = results.getString("name");
                        JSONArray photosArray = results.getJSONArray("photos");
                        String photoUrl = "http://images.gotinder.com/" + matches.get(iterator).getUserId() + "/" + photosArray.getJSONObject(0).getString("fileName");
                        matches.get(iterator).setName(name);
                        matches.get(iterator).setPhotoUrl(photoUrl);
                        if (iterator == (matches.size()-1)) {
                            searchField.setEnabled(true);
                        }
                        //Log.d("PDBug", "onResponsephotourl: "+photoUrl);
                    } catch (JSONException ex) {
                        Log.d("PDBug", "addUser: "+ex.getLocalizedMessage());
                    }
                }

                @Override
                public void onFail(String msg) {

                }
            });
            if (i == (matches.size()-1)) {
                mAdapter = new MatchAdapter(getApplicationContext(), R.layout.match_item, matches);
            }
        }
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                update();
            }
        });
        lw.setAdapter(mAdapter);
    }

    public void searchMatches(String search) {
        ArrayList<Match> searchResults = new ArrayList<>();
        for (int i = 0; i < matches.size(); i++) {
            String name = matches.get(i).getName();
            if (search.length() < name.length()) {
                //Doesn't match the last letter when writing full name ie. nanna will only show up till nann
                String subName = name.substring(0, search.length());
                if (search.equalsIgnoreCase(subName)) {
                    Log.d("PDBug", "searchMatches: " + matches.get(i).getName());
                    searchResults.add(matches.get(i));
                }
            }
        }
        mAdapter = new MatchAdapter(getApplicationContext(), R.layout.match_item, searchResults);
        lw.setAdapter(mAdapter);
    }

    public void update() {
        lw.setAdapter(mAdapter);
    }

    public void goBack(View v) {
        finish();
    }
}
