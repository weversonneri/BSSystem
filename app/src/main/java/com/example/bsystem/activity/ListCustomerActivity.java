package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bsystem.R;
import com.example.bsystem.adapter.CustomerAdapter;
import com.example.bsystem.model.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListCustomerActivity extends AppCompatActivity {

    private static final String TAG = "ListCustomerActivity";
    private ViewHolder mViewHolder = new ViewHolder();

    ArrayList<Customer> arrayList;

    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    Query referenceCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cliente);

        mAuth = FirebaseAuth.getInstance();

        this.mViewHolder.toolbar = findViewById(R.id.list_customer_toolbar);
        setSupportActionBar(this.mViewHolder.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        arrayList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceCustomer = firebaseDatabase.getReference("customer").orderByChild("name");

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

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Log.d(TAG, "Autenticado");
        } else {
            finish();
        }
    }


    public static class ViewHolder {
        ListView listView;
        FloatingActionButton buttonCadastro;
        Toolbar toolbar;

    }
}