package com.midterm.lasalle.testtweeter;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class TwitterApi extends TwitterApiClient {


    public TwitterApi(TwitterSession session) {
        super(session);
    }

    public GetUserApi getUserApi(){
        return getService(GetUserApi.class);
    }


    interface GetUserApi {
        @GET("/1.1/users/show.json")
        Call<User> show(@Query("user_id") long userId);

        @GET("/1.1/followers/list.json")
        Call<TwitterFollowers> getFollowers(@Query("user_id") long userId);
    }
}
