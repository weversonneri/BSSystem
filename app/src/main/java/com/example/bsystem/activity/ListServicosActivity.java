package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.R;
import com.example.bsystem.adapter.ServiceAdapter;
import com.example.bsystem.model.Service;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListServicosActivity extends AppCompatActivity {

    ArrayList<Service> arrayList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenceService;

    private ViewHolder mViewHolder = new ViewHolder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_servicos);

        arrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceService = firebaseDatabase.getReference("services");

        this.mViewHolder.listView = findViewById(R.id.list_servicos);

        this.mViewHolder.buttonCadastrar = findViewById(R.id.button_cadastro_servico);
        this.mViewHolder.buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListServicosActivity.this, AddServiceActivity.class);
                startActivity(intent);
            }
        });

        referenceService.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Service service = dataSnapshot.getValue(Service.class);
                    arrayList.add(service);

                }
                ServiceAdapter arrayAdapter = new ServiceAdapter(ListServicosActivity.this, R.layout.layout_list_service, arrayList);
                ListServicosActivity.this.mViewHolder.listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        this.mViewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Service selectService = (Service) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(ListServicosActivity.this, AddServiceActivity.class);
                intent.putExtra("services", selectService);
                startActivity(intent);

            }
        });
    }

    public static class ViewHolder {
        ListView listView;
        FloatingActionButton buttonCadastrar;
    }
}