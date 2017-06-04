package com.example.philip.chainsaw.model.test;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by philip on 5/4/17.
 */

public class Post {
    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;

    public Post() {

    }

    public Post(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    @Override
    public String toString() {
        return id + " " + title + "     " + body;
    }


    /*
    * Retrofit in the activity
    * Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        /*TinderServiceRetrofit service = retrofit.create(TinderServiceRetrofit.class);
        Call<Response> ss = service.authUser(token, String.valueOf(id));

        ss.enqueue(new CallBack<ResponseBody>() {
                       @Override
                       public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                           Log.d("PDBug", "onResponse: " + response.toString());
                       }

                       @Override
                       public void onFailure(Call<ResponseBody> call, Throwable t) {
                           Log.d("PDBug", "onFailure: " + t.getLocalizedMessage());
                       }
                   });
        /*Call<Post> plc = service.getPost(1);
        plc.enqueue(new CallBack<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                int statusCode = response.code();
                Post p = response.body();
                Log.d("Taggy", "onResponse: "+p.toString());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
        Call<List<Post>> call = service.getPosts();
        call.enqueue(new CallBack<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                int statusCode = response.code();
                List<Post> posts = response.body();
                Log.d("Taggy", "onResponse: LIST");
                for (Post post : posts) {
                    Log.d("Taggy", "onResponse: "+post.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });

        Call<Post> postCall = service.createPost(post);
        postCall.enqueue(new CallBack<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                int statusCode = response.code();
                Log.d("Taggy", "onResponse: "+statusCode);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });*/

}
