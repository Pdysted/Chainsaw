package com.example.philip.chainsaw.interfaces;

import com.example.philip.chainsaw.model.Match;
import com.example.philip.chainsaw.model.Rec;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by philip on 6/4/17.
 */

public interface CallBack {
        void onSuccess(JSONObject response);

        void onFail(String msg);
}
