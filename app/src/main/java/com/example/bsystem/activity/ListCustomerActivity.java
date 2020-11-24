package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.R;
import com.example.bsystem.adapter.CustomerAdapter;
import com.example.bsystem.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListCustomerActivity extends AppCompatActivity {

    private ViewHolder mViewHolder = new ViewHolder();

    ArrayList<Customer> arrayList;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenceCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cliente);

        arrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceCustomer = firebaseDatabase.getReference("customer");

        this.mViewHolder.listView = findViewById(R.id.list_clientes);

        this.mViewHolder.buttonCadastro = findViewById(R.id.button_cadastro_cliente);
        this.mViewHolder.buttonCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListCustomerActivity.this, AddCustomerActivity.class);
                startActivity(intent);
            }
        });

        referenceCustomer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Customer customer = dataSnapshot.getValue(Customer.class);
                    arrayList.add(customer);
                }
                CustomerAdapter arrayAdapter = new CustomerAdapter(ListCustomerActivity.this, R.layout.layout_list_customer, arrayList);
                ListCustomerActivity.this.mViewHolder.listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        this.mViewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Customer selectCustomer = (Customer) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(ListCustomerActivity.this, AddCustomerActivity.class);
                intent.putExtra("customer", selectCustomer);

                startActivity(intent);


            }
        });
    }


    public static class ViewHolder {
        ListView listView;
        FloatingActionButton buttonCadastro;

    }
}