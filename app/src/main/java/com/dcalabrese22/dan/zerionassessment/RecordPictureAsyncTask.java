package com.dcalabrese22.dan.zerionassessment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
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
public class RecordPictureAsyncTask extends AsyncTask<ZerionRecord, Void, Bitmap> {

    private WeakReference<ImageView> mImageView;

    public RecordPictureAsyncTask(ImageView imageView) {
        mImageView = new WeakReference<>(imageView);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = mImageView.get();
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected Bitmap doInBackground(ZerionRecord... zerionRecords) {
        return getRecordBitmap(zerionRecords[0].getPictureUrl());
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
