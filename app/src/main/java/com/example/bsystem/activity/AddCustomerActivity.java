package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.R;
import com.example.bsystem.model.Customer;
import com.example.bsystem.utils.TelefoneMaskUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    Customer customer;

    private ViewHolder mViewHolder = new ViewHolder();

    DatabaseReference referenceCustomer;
    FirebaseDatabase database;
    //  FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        database = FirebaseDatabase.getInstance();
        referenceCustomer = database.getReference("customer");
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
        if (AddCustomerActivity.this.mViewHolder.editTelefone.length() == 10) {
            AddCustomerActivity.this.mViewHolder.editTelefone.addTextChangedListener(TelefoneMaskUtil.insert("(##)####-####", AddCustomerActivity.this.mViewHolder.editTelefone));

        }
        return super.toString();
    }
};*/

        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra("customer");
        if (customer != null) {

            this.mViewHolder.editNome.setText(customer.getName());
            this.mViewHolder.editTelefone.setText(customer.getPhone());
            this.mViewHolder.editData.setText(customer.getBirthDate());
            this.mViewHolder.editEmail.setText(customer.getEmail());

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
            register();
        }
    }

    private void register() {

        String name = this.mViewHolder.editNome.getText().toString();
        String phone = this.mViewHolder.editTelefone.getText().toString();
        String birthDate = this.mViewHolder.editData.getText().toString();
        String email = this.mViewHolder.editEmail.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Preencher", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "phone", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(birthDate)) {
            Toast.makeText(this, "time", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "service", Toast.LENGTH_SHORT).show();
        } else {

            String id = referenceCustomer.push().getKey();

            Customer customer = new Customer(id,name, phone, birthDate, email);
            referenceCustomer.child(id).setValue(customer);

            Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_LONG).show();
            finish();

        }
    }


    public static class ViewHolder {
        MaterialButton buttonCadastro;
        MaterialButton buttonCancel;
        TextInputEditText editNome, editTelefone, editData, editEmail;
    }
}