package com.midterm.lasalle.testtweeter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TwitterSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        String username = getIntent().getStringExtra(MainActivity.TWITTER_USERNAME);

        TextView textViewUsername = findViewById(R.id.textViewUsername);
        textViewUsername.setText(username);


        new TwitterApi(session).getUserApi().show(session.getUserId()).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {

                TextView textViewUserInfo = findViewById(R.id.textViewUserInfo);
                String info = "Name =" + result.data.name + "\nLocation =" + result.data.location +
                        "\nFriends =" + result.data.friendsCount + "\nFollowers =" + result.data.followersCount;
                textViewUserInfo.setText(info);

                Picasso.with(getBaseContext()).load(result.data.profileImageUrlHttps).resize(200,200)
                        .into((ImageView)findViewById(R.id.userImageProfile));
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), "Network error!", Toast.LENGTH_SHORT).show();
            }
        });

        Button followersBtn = findViewById(R.id.followersBtn);
        followersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TwitterApi(session).getUserApi().getFollowers(session.getUserId()).enqueue(new Callback<TwitterFollowers>() {
                    @Override
                    public void success(Result<TwitterFollowers> result) {

                        ListView listView = findViewById(R.id.listView);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);

                        List<String> list = new ArrayList<>();

                        for(User user:result.data.getUsers()){
                            list.add(user.name);
                        }

                        adapter.addAll(list);
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(getApplicationContext(), "Error in getting users!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });






    }
}
