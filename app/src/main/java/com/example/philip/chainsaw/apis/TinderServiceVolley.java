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
                        onCallBack.onSuccess(response);
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
                    onCallBack.onSuccess(response);
                } catch (Exception e) {
                    Log.d("PDBug", "onResponseError: " + e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onCallBack.onFail("d");
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
                    onCallBack.onSuccess(response);
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
        String url = "https://api.gotinder.com/user/" + match.getUserId();
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    onCallBack.onSuccess(response);
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
        String url = "https://api.gotinder.com/user/matches/"+matchId;
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
                Log.d("PDBug", "onErrorResponse: "+error.getMessage()+"\n"+error.getStackTrace() +" "+ error.getLocalizedMessage()+
                error.toString());

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
