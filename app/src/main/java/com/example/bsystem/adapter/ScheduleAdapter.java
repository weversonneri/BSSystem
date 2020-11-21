package com.example.bsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bsystem.R;
import com.example.bsystem.model.Schedule;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ScheduleAdapter extends ArrayAdapter<Schedule> {

    private static final String TAG = "ScheduleAdapter";

    private Context mContext;
    private int mResource;
    //private int lastPosition = -1;

    public ScheduleAdapter(Context context, int resourse, ArrayList<Schedule> objects) {
        super(context, resourse, objects);
        mContext = context;
        mResource = resourse;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String time = getItem(position).getTime();

        Schedule schedule = new Schedule(name, time);

        final View result;

        ViewHolder mViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.name = (TextView) convertView.findViewById(R.id.text_schedule_list_name);
            mViewHolder.time = (TextView) convertView.findViewById(R.id.text_schedule_list_time);

            result = convertView;
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        DecimalFormat formatValor = new DecimalFormat("#,##0.00");

        mViewHolder.name.setText(schedule.getName());
        mViewHolder.time.setText(schedule.getTime());

        return convertView;
    }

    public static class ViewHolder {
        TextView name;
        TextView time;
    }

}
