package com.midterm.lasalle.testtweeter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class MainActivity extends AppCompatActivity {

    public final static String TWITTER_USERNAME = "TWITTER_USERNAME";
    public final static String TWITTER_TOKEN = "TWITTER_TOKEN";
    public final static String TWITTER_SECRET = "TWITTER_SECRET";

    TwitterLoginButton twitBtn;
    int counterEntry = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Twitter.initialize(this);
        setContentView(R.layout.activity_main);
        twitBtn = findViewById(R.id.twitBtn);

        Context context = this;
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.Login_To_Twitter), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int times = sharedPreferences.getInt("Enter", 0);
        if (times == 0) {

            twitBtn.setCallback(new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {
                    twitterLogin();
                }

                @Override
                public void failure(TwitterException exception) {
                    Toast.makeText(getApplicationContext(), "login failed!", Toast.LENGTH_SHORT).show();
                }
            });
            counterEntry += 1;
            editor.putInt("Enter", counterEntry);
            editor.commit();
        } else {
            twitterLogin();
        }
    }

    private void twitterLogin(){
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthToken authToken = session.getAuthToken();

        String username = session.getUserName();
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra(TWITTER_USERNAME, username);
        intent.putExtra(TWITTER_TOKEN, authToken.token);
        intent.putExtra(TWITTER_SECRET, authToken.secret);
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitBtn.onActivityResult(requestCode, resultCode,data);
    }
}
