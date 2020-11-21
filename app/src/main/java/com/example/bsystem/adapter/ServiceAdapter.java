package com.example.bsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bsystem.R;
import com.example.bsystem.model.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ServiceAdapter extends ArrayAdapter<Service> {

    private static final String TAG = "ServiceAdapter";

    private Context mContext;
    private int mResource;
    //private int lastPosition = -1;

    public ServiceAdapter(Context context, int resourse, ArrayList<Service> objects) {
        super(context, resourse, objects);
        mContext = context;
        mResource = resourse;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String name = getItem(position).getName();
        String price = getItem(position).getPrice();

        Service service = new Service(name, price);

        final View result;

        ViewHolder mViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.nome = (TextView) convertView.findViewById(R.id.text_list_servico_nome);
            mViewHolder.valor = (TextView) convertView.findViewById(R.id.text_list_servico_valor);

            result = convertView;
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        DecimalFormat formatValor = new DecimalFormat("#,##0.00");

        mViewHolder.nome.setText(service.getName());
        mViewHolder.valor.setText(service.getPrice());

        return convertView;
    }

    public static class ViewHolder {
        TextView nome;
        TextView valor;
    }

}
