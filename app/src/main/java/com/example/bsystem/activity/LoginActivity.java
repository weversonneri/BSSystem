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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewHolder mViewHolder = new ViewHolder();
    private FirebaseAuth mAuth;

    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

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
        mAuth.signInWithEmailAndPassword(this.mViewHolder.editLoginEmail.getText().toString(), this.mViewHolder.editLoginPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:sucesso");
                            Toast.makeText(LoginActivity.this, "Logado com sucesso", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            LoginActivity.this.mViewHolder.editLoginEmail.getText().clear();
                            LoginActivity.this.mViewHolder.editLoginPassword.getText().clear();

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

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Toast.makeText(this, "Logado com sucesso", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));

        } else {

        }
    }

    public static class ViewHolder {
        TextInputEditText editLoginEmail, editLoginPassword;
        TextInputLayout layoutLoginEmail, layoutLoginPassword;
        Button buttonLoginSubmit;
        TextView textLoginRegister;

    }
}