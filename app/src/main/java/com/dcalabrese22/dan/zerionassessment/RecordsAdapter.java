package com.dcalabrese22.dan.zerionassessment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dan on 12/23/17.
 */

/**
 * Adapter for RecordActivity ListView. Would have preferred to use RecyclerView, but instructions
 * specified ListView
 */
public class RecordsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ZerionRecord> mRecords;
    private LayoutInflater mInflater;

    public RecordsAdapter(Context context, ArrayList<ZerionRecord> records) {
        mContext = context;
        mRecords = records;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mRecords != null) {
            return mRecords.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return mRecords.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecordViewHolder viewHolder;
        if (view == null) {
            view = mInflater.inflate(R.layout.record, viewGroup, false);
            viewHolder = new RecordViewHolder();
            viewHolder.name = view.findViewById(R.id.tv_record_name);
            viewHolder.picture = view.findViewById(R.id.iv_record_picture);
            viewHolder.position = i;
            view.setTag(viewHolder);
        } else {
            viewHolder = (RecordViewHolder) view.getTag();
        }
        ZerionRecord record = (ZerionRecord) getItem(i);
        viewHolder.name.setText(record.getName());
        int position = viewHolder.position;
        if (viewHolder.picture != null) {
            new RecordPictureAsyncTask(record, position).execute(viewHolder);
        }

        return view;
    }

    public ArrayList<ZerionRecord> getData() {
        return mRecords;
    }


}
