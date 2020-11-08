package com.example.bsystem.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bsystem.MainActivity;
import com.example.bsystem.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textCriarConta;
    TextInputEditText editLoginEmail, editLoginSenha;
    Button buttonEntrar;

    private FirebaseAuth mAuth;

    private final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        textCriarConta = findViewById(R.id.text_login_criar_conta);
        textCriarConta.setOnClickListener(this);

        buttonEntrar = findViewById(R.id.button_login_entrar);
        buttonEntrar.setOnClickListener(this);

        editLoginEmail = findViewById(R.id.edit_login_email);
        editLoginSenha = findViewById(R.id.edit_login_senha);

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
        if (v.getId() == R.id.text_login_criar_conta) {
            Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.button_login_entrar) {

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            //login();
        }
    }

  /*  public void login() {
        mAuth.signInWithEmailAndPassword(editLoginEmail.getText().toString(), editLoginSenha.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(LoginActivity.this, "Authentication success", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            // ...
                        }

                    }
                });
    }*/

    private void updateUI(FirebaseUser user) {

    }


}