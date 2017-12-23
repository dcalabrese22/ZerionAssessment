package com.dcalabrese22.dan.zerionassessment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Activity for displaying a list of records
 */
public class RecordsActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private String mToken;
    private ArrayList<ZerionRecord> mRecords;
    private ListView mListView;
    private RecordsAdapter mAdapter;
    private Context mContext;

    public static final String EXTRA_RECORD_PIC = "record_pic";
    public static final String EXTRA_RECORD = "extra_record";
    public static final String SAVED_STATE_LIST = "saved_state";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        mContext = this;
        mProgressBar = findViewById(R.id.pb_records_loading);
        mListView = findViewById(R.id.lv_records_list);

        //open the record's details when it is pressed
        //pass the record details in a bundle to eliminate the need to make use of the network again
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ZerionRecord record = (ZerionRecord) mListView.getItemAtPosition(i);
                Bundle bundle = new Bundle();
                bundle.putParcelable(EXTRA_RECORD, record);

                //pass the image as byte array to avoid having to re-download it
                ImageView imageView = view.findViewById(R.id.iv_record_picture);
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                bundle.putByteArray(EXTRA_RECORD_PIC, bs.toByteArray());
                Intent intent = new Intent(mContext, RecordDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        mToken = intent.getStringExtra(AccessTokenActivity.ACCESS_TOKEN_EXTRA);

        if (savedInstanceState != null) {
            mRecords = savedInstanceState.getParcelableArrayList(SAVED_STATE_LIST);
            mAdapter = new RecordsAdapter(mContext, mRecords);
            mListView.setAdapter(mAdapter);
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mRecords = new ArrayList<>();
            new RecordsAsyncTask(mProgressBar).execute();
        }


    }

    //save the records list on rotation to avoid having to re-query
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_STATE_LIST, mRecords);
    }

    /**
     * Task that gets the record data in the background and populates the listview
     */
    private class RecordsAsyncTask extends AsyncTask<Void, Void, Void> {


        public RecordsAsyncTask(ProgressBar progressBar) {
            mProgressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getRecordData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter = new RecordsAdapter(mContext, mRecords);
            mListView.setAdapter(mAdapter);
        }
    }

    private static final String BASE_URL_FOR_RECORDS =
            "https://app.iformbuilder.com/exzact/api/v60/profiles/470670/pages/3644442/records";

    /**
     * Helper function to populate the list of records
     */
    public void getRecordData() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL_FOR_RECORDS);
            connection = (HttpURLConnection) url.openConnection();
            String bearer = "Bearer " + mToken;
            connection.setRequestProperty("Authorization", bearer);
            InputStream is = connection.getInputStream();
            String response = AccessTokenHelper.getFromInputStream(is);
            try {
                JSONArray array = new JSONArray(response);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    int id = object.getInt("id");
                    ZerionRecord record = getSingleRecord(id);
                    record.setId(id);
                    mRecords.add(record);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (MalformedURLException e) {
            Log.d("Malformed URL", e.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Helper function to get the data for a single record
     * @param id The id of the record to get
     * @return A record object
     */
    public ZerionRecord getSingleRecord(int id) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BASE_URL_FOR_RECORDS + "/" + id);
            connection = (HttpURLConnection) url.openConnection();
            String bearer = "Bearer " + mToken;
            connection.setRequestProperty("Authorization", bearer);
            InputStream is = connection.getInputStream();
            String response = AccessTokenHelper.getFromInputStream(is);
            try {
                JSONObject object = new JSONObject(response);
                String name = object.getString("name");
                int age = object.getInt("age");
                String phone = object.getString("phone");
                String date = object.getString("the_date");
                String picUrl = object.getString("picture");
                return new ZerionRecord(name, age, phone, date, picUrl);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (MalformedURLException e) {
            Log.d("Malformed URL", e.toString());
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }


}
