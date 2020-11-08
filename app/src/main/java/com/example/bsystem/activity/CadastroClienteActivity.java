package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.R;
import com.example.bsystem.model.Cliente;
import com.example.bsystem.utils.TelefoneMaskUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;


public class CadastroClienteActivity extends AppCompatActivity implements View.OnClickListener {

    Cliente cliente;

    private ViewHolder mViewHolder = new ViewHolder();

    DatabaseReference refCliente;
    FirebaseDatabase database;
    //  FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);

        database = FirebaseDatabase.getInstance();
        refCliente = database.getReference("clientes");
        //  firebaseAuth = FirebaseAuth.getInstance();


        this.mViewHolder.editNome = findViewById(R.id.edit_cadastro_cliente_nome);
        this.mViewHolder.editTelefone = findViewById(R.id.edit_cadastro_cliente_telefone);
        this.mViewHolder.editData = findViewById(R.id.edit_cadastro_cliente_data);
        this.mViewHolder.editEmail = findViewById(R.id.edit_cadastro_cliente_email);


        this.mViewHolder.buttonCadastro = findViewById(R.id.button_cadastro_cliente_confim);
        this.mViewHolder.buttonCancel = findViewById(R.id.button_cadastro_cliente_cancel);

        this.mViewHolder.buttonCancel.setOnClickListener(this);
        this.mViewHolder.buttonCadastro.setOnClickListener(this);


        this.mViewHolder.editTelefone.addTextChangedListener(TelefoneMaskUtil.insert("(##)#####-####", this.mViewHolder.editTelefone));
        this.mViewHolder.editData.addTextChangedListener(TelefoneMaskUtil.insert("##/##/####", this.mViewHolder.editData));





/*TelefoneMaskUtil test = new TelefoneMaskUtil() {
    @NonNull
    @Override
    public String toString() {
        if (CadastroClienteActivity.this.mViewHolder.editTelefone.length() == 10) {
            CadastroClienteActivity.this.mViewHolder.editTelefone.addTextChangedListener(TelefoneMaskUtil.insert("(##)####-####", CadastroClienteActivity.this.mViewHolder.editTelefone));

        }
        return super.toString();
    }
};*/

        Intent intent = getIntent();
        cliente = (Cliente) intent.getSerializableExtra("cliente");
        if (cliente != null) {

            this.mViewHolder.editNome.setText(cliente.getNome());
            this.mViewHolder.editTelefone.setText(cliente.getTelefone());
            this.mViewHolder.editData.setText(cliente.getNascimento());
            this.mViewHolder.editEmail.setText(cliente.getEmail());

        }

    }

  /* @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }*/

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_cadastro_cliente_cancel) {
            finish();
        }
        if (id == R.id.button_cadastro_cliente_confim) {
            cadastrar();
        }
    }

    private void cadastrar() {

        cliente = new Cliente();

        cliente.setNome(this.mViewHolder.editNome.getText().toString());
        cliente.setTelefone(this.mViewHolder.editTelefone.getText().toString());
        cliente.setNascimento(this.mViewHolder.editData.getText().toString());
        cliente.setEmail(this.mViewHolder.editEmail.getText().toString());

        refCliente.child(cliente.getTelefone()).setValue(cliente);

        Toast.makeText(this, "ok", Toast.LENGTH_LONG).show();
        finish();
    }


    public static class ViewHolder {
        MaterialButton buttonCadastro;
        MaterialButton buttonCancel;
        TextInputEditText editNome, editTelefone, editData, editEmail;
    }
}