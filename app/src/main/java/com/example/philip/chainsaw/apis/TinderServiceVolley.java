package com.example.philip.chainsaw.apis;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.philip.chainsaw.interfaces.CallBack;
import com.example.philip.chainsaw.model.Match;
import com.example.philip.chainsaw.model.Message;
import com.example.philip.chainsaw.model.Rec;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by philip on 5/21/17.
 */

public class TinderServiceVolley {
    //Can't be singleton as context is needed to instantiate the request queue
    private static TinderServiceVolley mInstance;
    private static Context context;
    RequestQueue reqQueue;

    private TinderServiceVolley(Context context) {
        this.context = context;
        reqQueue = getRequestQueue();
    }

    public static synchronized TinderServiceVolley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TinderServiceVolley(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (reqQueue == null) {
            reqQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return reqQueue;
    }


    public void auth(int facebookId, String accessToken, final CallBack onCallBack) {
        String url = "https://api.gotinder.com/auth";
        Map<String, String> params = new HashMap<String, String>();
        params.put("facebook_token", accessToken);
        params.put("facebook_id", String.valueOf(facebookId));
        JSONObject jsonObject = new JSONObject(params);
        final JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("PDBug", "onResponse: " + response.toString());
                        try {
                            JSONObject jsonObj = new JSONObject(response.toString());
                            String tinderToken = jsonObj.getString("token");
                            Log.d("PDBug", "onResponse: " + tinderToken);
                            onCallBack.onSuccessAuth(tinderToken);
                        } catch (JSONException e) {
                            Log.d("PDBug", "onResponse: "+e.getLocalizedMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //If the Facebook token has expired when authenticating
                        if (error.networkResponse.statusCode == 401) {
                            onCallBack.onFail("Authentication error: Facebook token expired");
                        }
                    }
                }) {
        };
        reqQueue.add(jsonObjRequest);
    }

    public void getRecs(final String tinderToken, final CallBack onCallBack) {
        String url = "https://api.gotinder.com/user/recs";
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Rec> tinderUsers = new ArrayList<>();
                    JSONArray users = response.getJSONArray("results");
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject jsonObj = users.getJSONObject(i);
                        int commonFriends = 0;
                        //jsonObj.getInt("common_friend_count") may cause errors due to no value
                        ArrayList<String> photosUrls = new ArrayList<>();
                        JSONArray photosArray = jsonObj.getJSONArray("photos");
                        for (int j = 0; j < photosArray.length(); j++) {
                            JSONObject photoJson = photosArray.getJSONObject(j);
                            photosUrls.add(photoJson.getString("url"));
                            //Log.d("PDBug", "photoArray: " + photoJson.getString("url"));
                        }
                        Rec user = new Rec(jsonObj.getInt("distance_mi"), commonFriends, jsonObj.getString("_id"),
                                jsonObj.getString("bio"), jsonObj.getInt("gender"), jsonObj.getString("name"), photosUrls);
                        tinderUsers.add(user);
                    }
                    onCallBack.onSuccessRecs(tinderUsers);
                    //Log.d("PDBug", "onResponse: " + users.getJSONObject(0).getJSONArray("photos").getJSONObject(0).getString("url"));
                    //Log.d("PDBug", "onResponse: " + users.getJSONObject(0).getJSONArray("photos").getJSONObject(0).getJSONArray("processedFiles").toString());
                    //Log.d("PDBug", "onResponsesss: "+ users.getJSONObject(0).getString("name"));
                } catch (Exception e) {
                    Log.d("PDBug", "onResponseError: " + e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Auth-Token", tinderToken);
                Log.d("PDBug", "getHeaders: " + params.toString());
                return params;
            }
        };
        reqQueue.add(jsonObjRequest);
    }

    public void likeUser(String userId, final String tinderToken) {
        //userId = "58cb16dd5ac3aa7e03bc6b12";
        String url = "https://api.gotinder.com/like/"+userId;
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("PDBug", "onResponse: " + response.toString());
                    if (response.getString("match").equals("true")) {
                        //Toast.makeText(context, "You matched!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", tinderToken);
                Log.d("PDBug", "getHeaders: " + params.toString());
                return params;
            }
        };
        reqQueue.add(jsonObjRequest);
    }

    public void passUser(String userId, final String tinderToken) {
        String url = "https://api.gotinder.com/pass/"+userId;
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("PDBug", "onResponse: " + response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", tinderToken);
                Log.d("PDBug", "getHeaders: " + params.toString());
                return params;
            }
        };
        reqQueue.add(jsonObjRequest);
    }

    public void getMessages(final String tinderToken, final CallBack onCallBack) {
        String url = "https://api.gotinder.com/updates";
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray matchesJson = response.getJSONArray("matches");
                    ArrayList<Match> matches = new ArrayList<>();
                    for (int i = 0; i < matchesJson.length() ; i++) {
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
                    }

                    Log.d("PDBug", "onResponseMatches: "+matches.size());
                    onCallBack.onSuccessMessages(matches);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PDBug", "onErrorResponse: "+error.getLocalizedMessage() + " " +error.networkResponse);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", tinderToken);
                Log.d("PDBug", "getHeaders: " + params.toString());
                return params;
            }
        };
        reqQueue.add(jsonObjRequest);
    }

    public void getUser(final Match match, final String tinderToken, final CallBack onCallBack) {
        //userId = "582217b3b9de8ec50a5033a0";
        String url = "https://api.gotinder.com/user/" + match.getUserId();
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("PDBug", "onResponseGetUser: " + response.toString());
                    JSONObject results = response.getJSONObject("results");
                    String name = results.getString("name");
                    JSONArray photosArray = results.getJSONArray("photos");
                    String photoUrl = "http://images.gotinder.com/" + match.getUserId() + "/" + photosArray.getJSONObject(0).getString("fileName");
                    //Log.d("PDBug", "onResponsephotourl: "+photoUrl);
                    onCallBack.onSuccessUser(match, name, photoUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", tinderToken);
                Log.d("PDBug", "getHeaders: " + params.toString());
                return params;
            }
        };
        reqQueue.add(jsonObjRequest);
    }

    public void sendMessage(final String matchId, final String tinderToken, final String message) throws JSONException {
        String url = "https://api.gotinder.com/user/matches/582217b3b9de8ec50a5033a0";
        JSONObject messageJson = new JSONObject();
        messageJson.put("message", message);
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, messageJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("PDBug", "responseSendMessage: " + response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PDBug", "onErrorResponse: "+error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", tinderToken);
                params.put("Content-Type", "application/json");
                Log.d("PDBug", "getHeaders: " + params.toString());
                return params;
            }
        };
        reqQueue.add(jsonObjRequest);
    }



}
