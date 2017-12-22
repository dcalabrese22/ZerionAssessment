package com.dcalabrese22.dan.zerionassessment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class AccessTokenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_token);

        String token = AccessTokenGenerator.createJwt(Constants.ISSUER);
        MyRequestQueue.getInstance(this).addToRequestQueue(AccessTokenGenerator.getAccessToken(token));
    }
}
