package com.dcalabrese22.dan.zerionassessment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dan on 12/23/17.
 */

/**
 * Class for the getting the picture of a record an placing in corresponding image view in the
 * background. Probably best to use Picasso or Glide, but no external libraries were to be used
 */
public class RecordPictureAsyncTask extends AsyncTask<RecordViewHolder, Void, Bitmap> {

    private ZerionRecord mRecord;
    private RecordViewHolder mViewHolder;
    private int mPosition;

    public RecordPictureAsyncTask(ZerionRecord record, int position) {
        mRecord = record;
        mPosition = position;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mViewHolder.position == mPosition) {
            mViewHolder.picture.setImageBitmap(bitmap);
        }

    }

    @Override
    protected Bitmap doInBackground(RecordViewHolder... viewHolders) {
        mViewHolder = viewHolders[0];
        return getRecordBitmap(mRecord.getPictureUrl());

    }


    private Bitmap getRecordBitmap(String urlString) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            return BitmapFactory.decodeStream(is);
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
