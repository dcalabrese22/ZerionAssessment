package com.dcalabrese22.dan.zerionassessment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Activity to display a single record's data
 */
public class RecordDetailActivity extends AppCompatActivity {

    private TextView mTextViewName;
    private TextView mTextViewAge;
    private TextView mTextViewPhone;
    private TextView mTextViewDate;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        mTextViewName = findViewById(R.id.tv_record_detail_name);
        mTextViewAge = findViewById(R.id.tv_record_detail_age);
        mTextViewPhone = findViewById(R.id.tv_record_detail_phone);
        mTextViewDate = findViewById(R.id.tv_record_detail_date);
        mImageView = findViewById(R.id.iv_record_detail_picture);

        Bundle extras = getIntent().getExtras();
        ZerionRecord record = extras.getParcelable(RecordsActivity.EXTRA_RECORD);
        String name = record.getName();
        int age = record.getAge();
        String phone = record.getPhone();
        String date = record.getDate();
        byte[] imageBytes = extras.getByteArray(RecordsActivity.EXTRA_RECORD_PIC);

        mTextViewName.setText(name);
        mTextViewAge.setText(String.valueOf(age));
        mTextViewPhone.setText(phone);
        mTextViewDate.setText(date);

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        mImageView.setImageBitmap(bitmap);
    }
}
