package com.example.philip.chainsaw.interfaces;

import com.example.philip.chainsaw.model.Match;
import com.example.philip.chainsaw.model.Rec;

import java.util.ArrayList;

/**
 * Created by philip on 6/4/17.
 */

public interface CallBack {
        void onSuccessAuth(String token);

        void onSuccessRecs(ArrayList<Rec> tinderUsers);

        void onSuccessMessages(ArrayList<Match> matches);

        void onSuccessUser(Match match, String name, String photoUrl);

        void onFail(String msg);
}
