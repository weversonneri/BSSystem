package com.example.bsystem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bsystem.R;
import com.example.bsystem.model.Servico;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ServicoListAdapter extends ArrayAdapter<Servico> {

    private static final String TAG = "ServicoListAdapter";

    private Context mContext;
    private int mResource;
    //private int lastPosition = -1;

    public ServicoListAdapter(Context context, int resourse, ArrayList<Servico> objects) {
        super(context, resourse, objects);
        mContext = context;
        mResource = resourse;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String nome = getItem(position).getDescricao();
        String valor = getItem(position).getValor().toString();

        Servico servico = new Servico(nome, Double.parseDouble(valor));

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

        mViewHolder.nome.setText(servico.getDescricao());
        mViewHolder.valor.setText(formatValor.format(servico.getValor()));

        return convertView;
    }

    public static class ViewHolder {
        TextView nome;
        TextView valor;
    }

}
