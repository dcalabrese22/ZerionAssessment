package com.dcalabrese22.dan.zerionassessment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Activity for getting an access token to access iformbuilder database
 */
public class AccessTokenActivity extends AppCompatActivity {

    Context mContext;
    public static final String ACCESS_TOKEN_EXTRA = "access_token_extra";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_token);

        mContext = this;

        Button getTokenBtn = findViewById(R.id.btn_get_access_token);

        getTokenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jwt = AccessTokenHelper.createJwt(Constants.ISSUER, Constants.SECRET_KEY);
                new AccessTokenAsyncTask(mContext).execute(jwt);
            }
        });
    }

    //Get the token in the background
    private class AccessTokenAsyncTask extends AsyncTask<String, Void, String> {

        private Context context;

        public AccessTokenAsyncTask(Context c) {
            context = c;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(context, RecordsActivity.class);
            intent.putExtra(ACCESS_TOKEN_EXTRA, s);
            context.startActivity(intent);
        }

        @Override
        protected String doInBackground(String... strings) {
            String jwt = strings[0];
            return AccessTokenHelper.getAccessToken(jwt);
        }
    }
}
