package com.example.bsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bsystem.R;
import com.example.bsystem.model.Customer;

import java.util.ArrayList;

public class CustomerAdapter extends ArrayAdapter<Customer> {

    private static final String TAG = "CustomerAdapter";

    private Context mContext;
    private int mResource;
    //private int lastPosition = -1;

    public CustomerAdapter(Context context, int resourse, ArrayList<Customer> objects) {
        super(context, resourse, objects);
        mContext = context;
        mResource = resourse;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String phone = getItem(position).getPhone();

        Customer customer = new Customer(name, phone);

        final View result;

        ViewHolder mViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.name = (TextView) convertView.findViewById(R.id.text_list_cliente_nome);
            mViewHolder.phone = (TextView) convertView.findViewById(R.id.text_list_cliente_telefone);

            result = convertView;
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        mViewHolder.name.setText(customer.getName());
        mViewHolder.phone.setText(customer.getPhone());

        return convertView;
    }

    public static class ViewHolder {
        TextView name;
        TextView phone;
    }

}
