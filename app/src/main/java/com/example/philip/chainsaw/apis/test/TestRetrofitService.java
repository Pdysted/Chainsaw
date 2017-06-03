package com.example.philip.chainsaw.apis.test;


import com.example.philip.chainsaw.model.test.Post;

import java.util.List;

import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by philip on 5/4/17.
 */

public interface TestRetrofitService {
    @GET("posts/{id}")
    Call<Post> getPost(@Path("id") int postId);

    @GET("posts")
    Call<List<Post>> getPosts();

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    //Tinder calls
    //Insert user model
    @POST("auth")
    Call<Post> authUser(@Query("facebook_token") String accessToken, @Query("facebook_id") String facebookId);


}

