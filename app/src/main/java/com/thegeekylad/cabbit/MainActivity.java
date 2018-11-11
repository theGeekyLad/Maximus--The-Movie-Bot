package com.thegeekylad.cabbit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.core.auth.AccessTokenManager;
import com.uber.sdk.android.core.auth.AuthenticationError;
import com.uber.sdk.android.core.auth.LoginCallback;
import com.uber.sdk.android.core.auth.LoginManager;
import com.uber.sdk.core.auth.AccessToken;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.rides.client.UberRidesApi;
import com.uber.sdk.rides.client.error.ApiError;
import com.uber.sdk.rides.client.error.ErrorParser;
import com.uber.sdk.rides.client.model.UserProfile;
import com.uber.sdk.rides.client.services.RidesService;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import java.io.IOException;
import java.util.Arrays;

import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoginCallback {

    private static final String CLIENT_ID = "QLrr5UViMfLj-MmYO9S2qRKStgsxEc1C";
    private static final String TOKEN = "sD46SyZZWOfSBri_gdW-AO1nHDbRvuvxJ6b6YI-W";

    private BotStuff cabbit;
    private LinearLayout messageContainer;
    private ScrollView scrollView;

    // uber
    private LoginManager loginManager;
    private RidesService ridesService;
    private UserProfile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permissions();

        messageContainer = (LinearLayout) findViewById(R.id.messageContainer);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        cabbit = new BotStuff(getApplicationContext());

        // first sync
        View replyView = getLayoutInflater().inflate(R.layout.layout_reply, null, false);
        ((TextView) replyView.findViewById(R.id.sourceField)).setText("Bot:");
        cabbit.hey("", (HtmlTextView) replyView.findViewById(R.id.messageField), true);
        messageContainer.addView(replyView);

        // send message
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ((EditText) findViewById(R.id.textField)).getText().toString();
                ((EditText) findViewById(R.id.textField)).setText("");
                View messageView, replyView;
                {
                    messageView = getLayoutInflater().inflate(R.layout.layout_message, null, false);
                    ((TextView) messageView.findViewById(R.id.sourceField)).setText("Me:");
                    ((TextView) messageView.findViewById(R.id.messageField)).setText(message.trim());
                    messageContainer.addView(messageView);
                }
                {
                    replyView = getLayoutInflater().inflate(R.layout.layout_reply, null, false);
                    ((TextView) replyView.findViewById(R.id.sourceField)).setText("Bot:");
                    cabbit.hey(message, (HtmlTextView) replyView.findViewById(R.id.messageField), true);
                    messageContainer.addView(replyView);
                }

                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });


            }
        });

        // clear
        findViewById(R.id.clearButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // first sync
                messageContainer.removeAllViewsInLayout();
                View replyView = getLayoutInflater().inflate(R.layout.layout_reply, null, false);
                ((TextView) replyView.findViewById(R.id.sourceField)).setText("Bot:");
                cabbit.hey("", (HtmlTextView) replyView.findViewById(R.id.messageField), false);
                messageContainer.addView(replyView);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginManager.onActivityResult(MainActivity.this, requestCode, resultCode, data);
    }

    @Override
    public void onLoginCancel() {

    }

    @Override
    public void onLoginError(@NonNull AuthenticationError error) {
        Toast.makeText(this, error.toStandardString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginSuccess(@NonNull AccessToken accessToken) {
        try {

            ridesService = UberRidesApi
                    .with(loginManager.getSession())
                    .build()
                    .createService();

            Response<UserProfile> response = ridesService
                    .getUserProfile()
                    .execute();

            if (response.isSuccessful()) {
                profile = response.body();
                Toast.makeText(this, "Hey "+profile.getFirstName()+"! Let's get started.", Toast.LENGTH_SHORT).show();
            } else {
                ApiError error = ErrorParser.parseError(response);
                Toast.makeText(this, "Weired, that flopped. :(", Toast.LENGTH_SHORT).show();
            }

            // pre-setup of env complete --------------------------------------------------------------------------------------------

            Toast.makeText(this, getProducts(), Toast.LENGTH_SHORT).show();

        } catch (IOException io) {
            Toast.makeText(this, io.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAuthorizationCodeReceived(@NonNull String authorizationCode) {

    }

    private void permissions() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
    }

    private void initUber() {
        SessionConfiguration sessionConfiguration = new SessionConfiguration.Builder()
                .setClientId(CLIENT_ID)
                .setServerToken(TOKEN)
                .setScopes(Arrays.asList(Scope.PROFILE, Scope.REQUEST))
                .setRedirectUri("https://login.uber.com/oauth/v2/authorize")
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();
        UberSdk.initialize(sessionConfiguration);
    }

    private LoginManager loginToUber() {
        AccessTokenManager accessTokenManager = new AccessTokenManager(getApplicationContext());
        LoginManager loginManager = new LoginManager(accessTokenManager, this);
        loginManager.loginForImplicitGrant(MainActivity.this);
        return loginManager;
    }

    private String getProducts() {
        try {
            return ridesService.getProducts(37.79f, -122.39f).execute().body().getProducts().get(0).getDisplayName();
        } catch (IOException io) {
            return io.getMessage();
        }
    }
}
