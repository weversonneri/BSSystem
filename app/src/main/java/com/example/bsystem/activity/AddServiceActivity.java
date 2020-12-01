package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.bsystem.R;
import com.example.bsystem.model.Service;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import me.abhinay.input.CurrencyEditText;


public class AddServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenceService;
    private boolean useCurrencySymbolAsHint;
    FirebaseAuth mAuth;

    String _ID, _NAME, _TYPE, _PRICE, _OBSERVATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);

        //  double cleanOutput = this.mViewHolder.editValor.getCleanDoubleValue();

        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceService = firebaseDatabase.getReference("services");

        mAuth = FirebaseAuth.getInstance();

        this.mViewHolder.toolbar = findViewById(R.id.add_service_toolbar);
        setSupportActionBar(this.mViewHolder.toolbar);

        this.mViewHolder.editDescricao = findViewById(R.id.edit_service_add_service);
        this.mViewHolder.editTipo = findViewById(R.id.edit_service_add_type);
        this.mViewHolder.editValor = findViewById(R.id.edit_service_add_price);
        this.mViewHolder.editObservacao = findViewById(R.id.edit_service_add_observation);

        this.mViewHolder.layoutDescricao = findViewById(R.id.layout_service_add_service);
        this.mViewHolder.layoutTipo = findViewById(R.id.layout_service_add_type);
        this.mViewHolder.layoutValor = findViewById(R.id.layout_service_add_price);
        this.mViewHolder.layoutObservacao = findViewById(R.id.layout_service_add_observation);

        this.mViewHolder.buttonSubmit = findViewById(R.id.button_service_add_submit);
        this.mViewHolder.buttonCancel = findViewById(R.id.button_service_add_cancel);

        this.mViewHolder.buttonCancel.setOnClickListener(this);
        this.mViewHolder.buttonSubmit.setOnClickListener(this);


        this.mViewHolder.editValor = (CurrencyEditText) findViewById(R.id.edit_service_add_price);
        this.mViewHolder.editValor.setCurrency("$");

        this.mViewHolder.editValor.setDelimiter(false);
        this.mViewHolder.editValor.setSpacing(false);
        this.mViewHolder.editValor.setDecimals(true);
        //Make sure that Decimals is set as false if a custom Separator is used
        // this.mViewHolder.editValor.setSeparator(".");


        showData();
    }

    private void showData() {
        Intent intent = getIntent();
        Service service = (Service) intent.getSerializableExtra("services");
        if (service != null) {

            this.mViewHolder.toolbar.setTitle("Serviço");

            this.mViewHolder.buttonSubmit.setVisibility(View.INVISIBLE);
            this.mViewHolder.buttonCancel.setVisibility(View.INVISIBLE);

            this.mViewHolder.editDescricao.setEnabled(false);
            this.mViewHolder.editTipo.setEnabled(false);
            this.mViewHolder.editValor.setEnabled(false);
            this.mViewHolder.editObservacao.setEnabled(false);

            _ID = service.getId();
            _NAME = service.getName();
            _TYPE = service.getType();
            _PRICE = service.getPrice();
            _OBSERVATION = service.getObservation();

            this.mViewHolder.editDescricao.setText(_NAME);
            this.mViewHolder.editTipo.setText(_TYPE);
            this.mViewHolder.editValor.setText(_PRICE);
            this.mViewHolder.editObservacao.setText(_OBSERVATION);
        }
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

        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_service_add_cancel) {
            finish();
        }
        if (id == R.id.button_service_add_submit) {
            register();
        }
    }

    private void register() {

        String name = this.mViewHolder.editDescricao.getText().toString();
        String type = this.mViewHolder.editTipo.getText().toString();
        String price = this.mViewHolder.editValor.getText().toString();
        String observation = this.mViewHolder.editObservacao.getText().toString();


        if (TextUtils.isEmpty(name)) {
            this.mViewHolder.layoutDescricao.setError("* Campo obrigatório");
            this.mViewHolder.layoutDescricao.requestFocus();
            return;
        } else {
            this.mViewHolder.layoutDescricao.setError(null);
        }
        if (TextUtils.isEmpty(type)) {
            this.mViewHolder.layoutTipo.setError("* Campo obrigatório");
        } else {
            this.mViewHolder.layoutTipo.setError(null);
        }
        if (TextUtils.isEmpty(price)) {
            this.mViewHolder.layoutValor.setError("* Campo obrigatório");
        } else if (price.length() > 10) {
            this.mViewHolder.layoutValor.setError("Valor inválido");
        } else{
            this.mViewHolder.layoutValor.setError(null);
        }
        if (TextUtils.isEmpty(observation)) {
            this.mViewHolder.layoutObservacao.setError("* Campo obrigatório");
        } else {
            if (_ID == null) {
                String id = referenceService.push().getKey();

                Service service = new Service(id, name, type, price, observation);
                referenceService.child(id).setValue(service).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddServiceActivity.this, "Novo serviço cadastrado", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(AddServiceActivity.this, "ERRO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                update();
            }
        }
    }

    private void update() {
        Service service = new Service(_ID, _NAME, _TYPE, _PRICE, _OBSERVATION);
        referenceService.child(_ID).child("name").setValue(this.mViewHolder.editDescricao.getText().toString());
        _NAME = this.mViewHolder.editDescricao.getText().toString();

        referenceService.child(_ID).child("type").setValue(this.mViewHolder.editTipo.getText().toString());
        _TYPE = this.mViewHolder.editTipo.getText().toString();

        referenceService.child(_ID).child("price").setValue(this.mViewHolder.editValor.getText().toString());
        _PRICE = this.mViewHolder.editValor.getText().toString();

        referenceService.child(_ID).child("observation").setValue(this.mViewHolder.editObservacao.getText().toString());
        _OBSERVATION = this.mViewHolder.editObservacao.getText().toString();

        Toast.makeText(this, "Dados atualizados", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit:

                serviceEdit();
                return true;

            case R.id.menu_item_remove:
                referenceService.child(_ID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddServiceActivity.this, "Serviço deletado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddServiceActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void serviceEdit() {

        this.mViewHolder.buttonSubmit.setVisibility(View.VISIBLE);
        this.mViewHolder.buttonCancel.setVisibility(View.VISIBLE);

        this.mViewHolder.buttonSubmit.setText("Salvar");

        this.mViewHolder.editDescricao.setEnabled(true);
        this.mViewHolder.editTipo.setEnabled(true);
        this.mViewHolder.editValor.setEnabled(true);
        this.mViewHolder.editObservacao.setEnabled(true);
    }

    public static class ViewHolder {
        Button buttonSubmit;
        Button buttonCancel;
        TextInputEditText editDescricao, editTipo, editObservacao;
        CurrencyEditText editValor;
        TextInputLayout layoutDescricao, layoutTipo, layoutObservacao, layoutValor;

        Toolbar toolbar;
    }
}