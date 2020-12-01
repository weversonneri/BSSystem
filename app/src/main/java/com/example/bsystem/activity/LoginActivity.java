package com.example.bsystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bsystem.MainActivity;
import com.example.bsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private FirebaseAuth mAuth;

    private final String TAG = "LoginActivity";

    FirebaseDatabase database;
    //DatabaseReference database;

    //DatabaseReference referenceLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        this.mViewHolder.layoutLoginEmail = findViewById(R.id.layout_login_email);
        this.mViewHolder.layoutLoginPassword = findViewById(R.id.layout_login_password);

        this.mViewHolder.textLoginRegister = findViewById(R.id.text_login_register);
        this.mViewHolder.textLoginRegister.setOnClickListener(this);

        this.mViewHolder.buttonLoginSubmit = findViewById(R.id.button_login_submit);
        this.mViewHolder.buttonLoginSubmit.setOnClickListener(this);

        this.mViewHolder.editLoginEmail = findViewById(R.id.edit_login_email);
        this.mViewHolder.editLoginPassword = findViewById(R.id.edit_login_password);

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
        if (v.getId() == R.id.text_login_register) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.button_login_submit) {

            String email = this.mViewHolder.editLoginEmail.getText().toString();
            String senha = this.mViewHolder.editLoginPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                this.mViewHolder.layoutLoginEmail.setError("Digite o email");
            } else if (TextUtils.isEmpty(senha)) {
                this.mViewHolder.layoutLoginPassword.setError("Digite a senha");
            } else {
                this.mViewHolder.layoutLoginEmail.setError(null);
                this.mViewHolder.layoutLoginPassword.setError(null);
                login();
            }
        }
    }

    public void login() {

        /*mAuth.signInWithEmailAndPassword(this.mViewHolder.editLoginEmail.getText().toString(), this.mViewHolder.editLoginPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });*/

        mAuth.signInWithEmailAndPassword(this.mViewHolder.editLoginEmail.getText().toString(), this.mViewHolder.editLoginPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);

                    check(task.getResult().getUser().getUid());


                    //   Log.d(TAG, "nao Autenticado" + snapshot.getValue());
              /*   String test = snapshot.getValue(String.class);

                        Log.d(TAG, "Autenticado" + test);
                        Toast.makeText(LoginActivity.this, "assssssssssdasdasdasd", Toast.LENGTH_SHORT).show();


               }else {
                        Log.d(TAG, "nao Autenticado");
                    }*/



                          /*  database.child("user").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                        String role = childSnapshot.getKey();
                                   /* String role = snapshot.getValue(String.class);*/
                                   /* if (role.equals("user"))

                                        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        Toast.makeText(LoginActivity.this, "admin", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "signInWithEmail:sucesso");

                                    }}

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.d(TAG, "signInWithEmail:n sucesso");


                                }
                            });
                            /*LoginActivity.this.mViewHolder.editLoginEmail.getText().clear();
                            LoginActivity.this.mViewHolder.editLoginPassword.getText().clear();*/

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "Email ou senha inválida",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                    // ...
                }

            }
        });
    }

    private void check(String uid) {

        DatabaseReference check = database.getReference("user").child(uid);

        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Log.d("User key", snapshot.getKey());
                String value = snapshot.child("role").getValue(String.class);
                if (value != null && value.equals("admin")) {
                    Log.d(TAG, "Level access is: " + value );

                    //System.out.println();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else if (value != null && value.equals("staff")) {
                    Log.d(TAG, "Level access is: " + value );

                    //System.out.println();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Log.d(TAG, "ERRO");
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "ERRO");
            }
        });

    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            // Toast.makeText(this, "Logado com sucesso", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(this, MainActivity.class));
            Log.d(TAG, "Autenticado");

        } else {
            Log.d(TAG, "Não autenticado");
        }
    }

    public static class ViewHolder {
        TextInputEditText editLoginEmail, editLoginPassword;
        TextInputLayout layoutLoginEmail, layoutLoginPassword;
        Button buttonLoginSubmit;
        TextView textLoginRegister;

    }
}