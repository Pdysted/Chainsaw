package com.example.philip.chainsaw;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.philip.chainsaw.adapters.MatchAdapter;
import com.example.philip.chainsaw.apis.TinderServiceVolley;
import com.example.philip.chainsaw.model.Match;
import com.example.philip.chainsaw.model.Rec;

import java.util.ArrayList;

public class MessagesActivity extends AppCompatActivity {
    private ListView lw;
    private String tinderToken;
    TinderServiceVolley tsv;
    MatchAdapter mAdapter;

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
                i.putExtra("NAME", m.getName());
                i.putExtra("PHOTO_URL", m.getPhotoUrl());
                i.putExtra("MESSAGES", m.getMessages());
                startActivity(i);
            }
        });
        tinderToken = getIntent().getStringExtra("TINDER_TOKEN");
        tsv = new TinderServiceVolley(getApplicationContext());
        tsv.getMessages(tinderToken, new TinderServiceVolley.CallBack() {
            @Override
            public void onSuccessAuth(String token) {

            }

            @Override
            public void onSuccessRecs(ArrayList<Rec> tinderUsers) {

            }

            @Override
            public void onSuccessMessages(ArrayList<Match> matches) {
                for (int i = 0; i < matches.size(); i++) {
                    tsv.getUser(matches.get(i), tinderToken, new TinderServiceVolley.CallBack() {
                        @Override
                        public void onSuccessAuth(String token) {

                        }

                        @Override
                        public void onSuccessRecs(ArrayList<Rec> tinderUsers) {

                        }

                        @Override
                        public void onSuccessMessages(ArrayList<Match> matches) {

                        }

                        @Override
                        public void onSuccessUser(Match match, String name, String photoUrl) {
                            match.setName(name);
                            match.setPhotoUrl(photoUrl);

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });
                    if (i == matches.size()-1) {
                        mAdapter = new MatchAdapter(getApplicationContext(), R.layout.match_item, matches);
                    }
                }

                mAdapter = new MatchAdapter(getApplicationContext(), R.layout.match_item, matches);
                mAdapter.registerDataSetObserver(new DataSetObserver() {
                    @Override
                    public void onChanged() {
                        update();
                    }
                });
                lw.setAdapter(mAdapter);
            }

            @Override
            public void onSuccessUser(Match match, String name, String photoUrl) {

            }

            @Override
            public void onFail(String msg) {

            }
        });

    }

    public void update() {
        lw.setAdapter(mAdapter);
    }

    public void goBack(View v) {
        finish();
    }
}
