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
import com.example.bsystem.model.Customer;
import com.example.bsystem.model.Schedule;
import com.example.bsystem.utils.TelefoneMaskUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddCustomerActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();

    DatabaseReference referenceCustomer;
    FirebaseDatabase database;
    FirebaseAuth mAuth;

    String _ID, _NAME, _PHONE, _BIRTHDATE, _EMAIL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        database = FirebaseDatabase.getInstance();
        referenceCustomer = database.getReference("customer");
        mAuth = FirebaseAuth.getInstance();


        this.mViewHolder.toolbar = findViewById(R.id.add_custome_toolbar);
        setSupportActionBar(this.mViewHolder.toolbar);

        this.mViewHolder.editCustomerAddName = findViewById(R.id.edit_customer_add_name);
        this.mViewHolder.editCustomerAddPhone = findViewById(R.id.edit_customer_add_phone);
        this.mViewHolder.editCustomerAddBirthDate = findViewById(R.id.edit_customer_add_birthDate);
        this.mViewHolder.editCustomerAddEmail = findViewById(R.id.edit_customer_add_email);

        this.mViewHolder.layoutCustomerAddName = findViewById(R.id.layout_customer_add_name);
        this.mViewHolder.layoutCustomerAddPhone = findViewById(R.id.layout_customer_add_phone);
        this.mViewHolder.layoutCustomerAddBirthDate = findViewById(R.id.layout_customer_add_birthDate);
        this.mViewHolder.layoutCustomerAddEmail = findViewById(R.id.layout_customer_add_email);

        this.mViewHolder.buttonSubmit = findViewById(R.id.button_customer_add_submit);
        this.mViewHolder.buttonCancel = findViewById(R.id.button_customer_add_cancel);

        this.mViewHolder.buttonCancel.setOnClickListener(this);
        this.mViewHolder.buttonSubmit.setOnClickListener(this);


        this.mViewHolder.editCustomerAddPhone.addTextChangedListener(TelefoneMaskUtil.insert("(##)#####-####", this.mViewHolder.editCustomerAddPhone));
        this.mViewHolder.editCustomerAddBirthDate.addTextChangedListener(TelefoneMaskUtil.insert("##/##/####", this.mViewHolder.editCustomerAddBirthDate));


        // this.mViewHolder.editCustomerAddPhone.addTextChangedListener(TelefoneMaskUtil.insert("(##)####-####", AddCustomerActivity.this.mViewHolder.editCustomerAddPhone));


        showData();

    }

    private void showData() {
        Intent intent = getIntent();
        Customer customer = (Customer) intent.getSerializableExtra("customer");
        if (customer != null) {

            this.mViewHolder.toolbar.setTitle("Cliente");

            this.mViewHolder.buttonSubmit.setVisibility(View.INVISIBLE);
            this.mViewHolder.buttonCancel.setVisibility(View.INVISIBLE);

            this.mViewHolder.editCustomerAddName.setEnabled(false);
            this.mViewHolder.editCustomerAddPhone.setEnabled(false);
            this.mViewHolder.editCustomerAddBirthDate.setEnabled(false);
            this.mViewHolder.editCustomerAddEmail.setEnabled(false);

            _ID = customer.getId();
            _NAME = customer.getName();
            _PHONE = customer.getPhone();
            _BIRTHDATE = customer.getBirthDate();
            _EMAIL = customer.getEmail();

            this.mViewHolder.editCustomerAddName.setText(_NAME);
            this.mViewHolder.editCustomerAddPhone.setText(_PHONE);
            this.mViewHolder.editCustomerAddBirthDate.setText(_BIRTHDATE);
            this.mViewHolder.editCustomerAddEmail.setText(_EMAIL);


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_customer_add_cancel:
                finish();
                break;

            case R.id.button_customer_add_submit:
                register();
                break;
        }
    }

    private void register() {

        String name = this.mViewHolder.editCustomerAddName.getText().toString();
        String phone = this.mViewHolder.editCustomerAddPhone.getText().toString();
        String birthDate = this.mViewHolder.editCustomerAddBirthDate.getText().toString();
        String email = this.mViewHolder.editCustomerAddEmail.getText().toString();

        if (TextUtils.isEmpty(name)) {
            this.mViewHolder.layoutCustomerAddName.setError("* Campo obrigatório");
        } else {
            this.mViewHolder.layoutCustomerAddName.setError(null);
        }
        if (TextUtils.isEmpty(phone)) {
            this.mViewHolder.layoutCustomerAddPhone.setError("* Campo obrigatório");
        }else if (phone.length() < 14) {
            this.mViewHolder.layoutCustomerAddPhone.setError("Telefone inválido");
        } else {
            this.mViewHolder.layoutCustomerAddPhone.setError(null);
        }
        if (TextUtils.isEmpty(birthDate)) {
            this.mViewHolder.layoutCustomerAddBirthDate.setError("* Campo obrigatório");
        } else if (birthDate.length() < 10) {
            this.mViewHolder.layoutCustomerAddBirthDate.setError("Data inválida");
        }else {
            this.mViewHolder.layoutCustomerAddBirthDate.setError(null);
        }
        if (TextUtils.isEmpty(email)) {
            this.mViewHolder.layoutCustomerAddEmail.setError("* Campo obrigatório");
        } else {
            if (_ID == null) {
                String id = referenceCustomer.push().getKey();

                Customer customer = new Customer(id, name, phone, birthDate, email);
                referenceCustomer.child(id).setValue(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddCustomerActivity.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            Toast.makeText(AddCustomerActivity.this, "ERRO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                update();
            }
        }
    }

    private void update() {
        Customer customer = new Customer(_ID, _NAME, _PHONE, _BIRTHDATE, _EMAIL);
        referenceCustomer.child(_ID).child("name").setValue(this.mViewHolder.editCustomerAddName.getText().toString());
        _NAME = this.mViewHolder.editCustomerAddName.getText().toString();

        referenceCustomer.child(_ID).child("phone").setValue(this.mViewHolder.editCustomerAddPhone.getText().toString());
        _PHONE = this.mViewHolder.editCustomerAddPhone.getText().toString();

        referenceCustomer.child(_ID).child("birthDate").setValue(this.mViewHolder.editCustomerAddBirthDate.getText().toString());
        _BIRTHDATE = this.mViewHolder.editCustomerAddBirthDate.getText().toString();

        referenceCustomer.child(_ID).child("email").setValue(this.mViewHolder.editCustomerAddEmail.getText().toString());
        _EMAIL = this.mViewHolder.editCustomerAddEmail.getText().toString();

        Toast.makeText(this, "Dados atualizados", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {

        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.edit_menu_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_edit_customer:

                customerEdit();
                return true;

            case R.id.menu_item_remove_customer:
                referenceCustomer.child(_ID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddCustomerActivity.this, "Cadastro deletado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AddCustomerActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                finish();

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void customerEdit() {
        this.mViewHolder.buttonSubmit.setVisibility(View.VISIBLE);
        this.mViewHolder.buttonCancel.setVisibility(View.VISIBLE);

        this.mViewHolder.buttonSubmit.setText("Salvar");

        this.mViewHolder.editCustomerAddName.setEnabled(true);
        this.mViewHolder.editCustomerAddPhone.setEnabled(true);
        this.mViewHolder.editCustomerAddBirthDate.setEnabled(true);
        this.mViewHolder.editCustomerAddEmail.setEnabled(true);
    }

    public static class ViewHolder {
        Button buttonSubmit;
        Button buttonCancel;
        TextInputEditText editCustomerAddName, editCustomerAddPhone, editCustomerAddBirthDate, editCustomerAddEmail;
        TextInputLayout layoutCustomerAddName, layoutCustomerAddPhone, layoutCustomerAddBirthDate, layoutCustomerAddEmail;
        Toolbar toolbar;
    }
}