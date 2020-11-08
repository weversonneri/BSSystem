package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.R;
import com.example.bsystem.adapter.ClienteListAdapter;
import com.example.bsystem.model.Cliente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListClienteActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();

    ArrayList<Cliente> arrayList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference refCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cliente);

        arrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        refCliente = firebaseDatabase.getReference("clientes");

        this.mViewHolder.listView = findViewById(R.id.list_clientes);

        this.mViewHolder.buttonCadastro = findViewById(R.id.button_cadastro_cliente);
        this.mViewHolder.buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListClienteActivity.this, CadastroClienteActivity.class);
                startActivity(intent);
            }
        });

        refCliente.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Cliente cliente = dataSnapshot.getValue(Cliente.class);
                    arrayList.add(cliente);
                }
                ClienteListAdapter arrayAdapter = new ClienteListAdapter(ListClienteActivity.this, R.layout.layout_list_cliente, arrayList);
                ListClienteActivity.this.mViewHolder.listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        this.mViewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Cliente selectCliente = (Cliente) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(ListClienteActivity.this, CadastroClienteActivity.class);
                intent.putExtra("cliente", selectCliente);

                startActivity(intent);


            }
        });
    }


    public static class ViewHolder {
        ListView listView;
        FloatingActionButton buttonCadastro;

    }
}