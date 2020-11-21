package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.R;
import com.example.bsystem.model.Schedule;
import com.example.bsystem.model.Service;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import me.abhinay.input.CurrencyEditText;


public class AddServiceActivity extends AppCompatActivity implements View.OnClickListener {

    Service service;

    private ViewHolder mViewHolder = new ViewHolder();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenceService;
    private boolean useCurrencySymbolAsHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

      //  double cleanOutput = this.mViewHolder.editValor.getCleanDoubleValue();

        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceService = firebaseDatabase.getReference("services");

        this.mViewHolder.editDescricao = findViewById(R.id.edit_service_add_service);
        this.mViewHolder.editTipo = findViewById(R.id.edit_service_add_type);
        this.mViewHolder.editValor = findViewById(R.id.edit_service_add_price);
        this.mViewHolder.editObservacao = findViewById(R.id.edit_service_add_observation);

        this.mViewHolder.buttonCadastro = findViewById(R.id.button_service_add_confirm);
        this.mViewHolder.buttonCancel = findViewById(R.id.button_service_add_cancel);

        this.mViewHolder.buttonCancel.setOnClickListener(this);
        this.mViewHolder.buttonCadastro.setOnClickListener(this);


        this.mViewHolder.editValor = (CurrencyEditText) findViewById(R.id.edit_service_add_price);
        this.mViewHolder.editValor.setCurrency("$");

        this.mViewHolder.editValor.setDelimiter(false);
        this.mViewHolder.editValor.setSpacing(false);
        this.mViewHolder.editValor.setDecimals(true);
        //Make sure that Decimals is set as false if a custom Separator is used
       // this.mViewHolder.editValor.setSeparator(".");

        Intent intent = getIntent();
        service = (Service) intent.getSerializableExtra("services");
        if (service != null) {

            this.mViewHolder.editDescricao.setText(service.getName());
            this.mViewHolder.editTipo.setText(service.getType());
            this.mViewHolder.editValor.setText(service.getPrice());
            this.mViewHolder.editObservacao.setText(service.getObservation());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_service_add_cancel) {
            finish();
        }
        if (id == R.id.button_service_add_confirm) {
            cadastrar();
        }
    }

    private void cadastrar() {

        String name = this.mViewHolder.editDescricao.getText().toString();
        String type = this.mViewHolder.editTipo.getText().toString();
        String price = this.mViewHolder.editValor.getText().toString();
        String observation = this.mViewHolder.editObservacao.getText().toString();


        if (TextUtils.isEmpty(name)) {
            this.mViewHolder.editDescricao.setError("sd");
            Toast.makeText(this, "Preencher", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(type)) {
            Toast.makeText(this, "phone", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "time", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(observation)) {
            Toast.makeText(this, "service", Toast.LENGTH_SHORT).show();
        } else {
            String id = referenceService.push().getKey();

            Service service = new Service(id, name, type, price, observation);
            referenceService.child(id).setValue(service);
            Toast.makeText(this, "Serviço cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    public static class ViewHolder {
        MaterialButton buttonCadastro;
        MaterialButton buttonCancel;
        TextInputEditText editDescricao, editTipo, editObservacao;
        CurrencyEditText  editValor;
    }
}