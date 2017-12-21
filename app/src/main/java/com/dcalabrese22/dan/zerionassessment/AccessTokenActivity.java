package com.dcalabrese22.dan.zerionassessment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class AccessTokenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_token);

        Log.d("Sig", AccessTokenGenerator.createToken("3e5065eb19bdf3ca90cc1fea582b758103cda27f"));
    }
}
