package com.example.bsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bsystem.R;
import com.example.bsystem.model.Cliente;

import java.util.ArrayList;

public class ClienteListAdapter extends ArrayAdapter<Cliente> {

    private static final String TAG = "ClienteListAdapter";

    private Context mContext;
    private int mResource;
    //private int lastPosition = -1;

    public ClienteListAdapter(Context context, int resourse, ArrayList<Cliente> objects) {
        super(context, resourse, objects);
        mContext = context;
        mResource = resourse;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String nome = getItem(position).getNome();
        String telefone = getItem(position).getTelefone();

        Cliente cliente = new Cliente(nome, telefone);

        final View result;

        ViewHolder mViewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            mViewHolder = new ViewHolder();
            mViewHolder.nome = (TextView) convertView.findViewById(R.id.text_list_cliente_nome);
            mViewHolder.telefone = (TextView) convertView.findViewById(R.id.text_list_cliente_telefone);

            result = convertView;
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        mViewHolder.nome.setText(cliente.getNome());
        mViewHolder.telefone.setText(cliente.getTelefone());

        return convertView;
    }

    public static class ViewHolder {
        TextView nome;
        TextView telefone;
    }

}
