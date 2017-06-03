package com.example.philip.chainsaw.apis.test;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.philip.chainsaw.model.test.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Created by philip on 5/4/17.
 */

public class TestVolleyService {
    Context context;

    public TestVolleyService(Context context) {
        this.context = context;
    }

    public void auth(int facebookId, String accessToken) {
        String url = "https://api.gotinder.com/auth";
        Map<String, String> params = new HashMap<String, String>();
        params.put("facebook_token", accessToken);
        params.put("facebook_id", String.valueOf(facebookId));
        JSONObject jsonObject = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(context);
        final JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Log.d("PDBug", "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        Log.d("PDBug", "onErrorResponse: "+error.toString());
                    }
                });
        queue.add(jsonObjRequest);
    }


    public Post getPost(int id) {
        String url = "https://jsonplaceholder.typicode.com/posts/" + id;
        ArrayList<Post> result;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Post post = new Post(response.getInt("userId"), response.getInt("id"), response.getString("title"), response.getString("body"));
                    Log.d("PDBug", "onResponse: " + post.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjRequest);
        return null;
    }

    public List<Post> getPosts() {
        String url = "https://jsonplaceholder.typicode.com/posts";
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    ArrayList<Post> posts = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObj = response.getJSONObject(i);
                        Post post = new Post(jsonObj.getInt("userId"), jsonObj.getInt("id"), jsonObj.getString("title"), jsonObj.getString("body"));
                        posts.add(post);
                        Log.d("PDBug", "onResponse: " + post.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
        return null;
    }


    public void createPost(final Post post) {
        String url = "http://anbo-canteen.azurewebsites.net/Service1.svc/ratings";
        Map<String, String> params = new HashMap<String, String>();
        params.put("CustomerId", String.valueOf(10));
        params.put("DishId", String.valueOf(4));
        params.put("Rate", String.valueOf(10));
        //params.put("body", post.getBody());
        JSONObject jsonObject = new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(context);
        final JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Log.d("PDBug", "onResponse: " + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        Log.d("PDBug", "onErrorResponse: "+error.toString());
                    }
                });
        queue.add(jsonObjRequest);
    }
}
