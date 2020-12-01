package com.example.bsystem.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bsystem.MainActivity;
import com.example.bsystem.R;
import com.example.bsystem.model.Schedule;
import com.example.bsystem.model.User;
import com.example.bsystem.utils.TelefoneMaskUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "";
    private ViewHolder mViewHolder = new ViewHolder();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference referenceUser;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        referenceUser = firebaseDatabase.getReference("user");

        this.mViewHolder.editRegisterAddName = findViewById(R.id.edit_register_add_name);
        this.mViewHolder.editRegisterAddEmail = findViewById(R.id.edit_register_add_email);
        this.mViewHolder.editRegisterAddPhone = findViewById(R.id.edit_register_add_phone);
        this.mViewHolder.editRegisterAddPassword = findViewById(R.id.edit_register_add_password);

        this.mViewHolder.cancel = findViewById(R.id.button_register_add_cancel);

        this.mViewHolder.submit = findViewById(R.id.button_register_add_submit);


        this.mViewHolder.editRegisterAddPhone.addTextChangedListener(TelefoneMaskUtil.insert("(##)#####-####", this.mViewHolder.editRegisterAddPhone));

        this.mViewHolder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.mViewHolder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
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
        if(currentUser != null){
            Toast.makeText(this,"U Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));

        }else {

        }
    }

    private void register() {
        String name = this.mViewHolder.editRegisterAddName.getText().toString();
        String phone = this.mViewHolder.editRegisterAddPhone.getText().toString();
        String email = this.mViewHolder.editRegisterAddEmail.getText().toString();
        String password = this.mViewHolder.editRegisterAddPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Preencher", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "phone", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "time", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "service", Toast.LENGTH_SHORT).show();
        }else if (password.length() < 6){
            Toast.makeText(this, "menor q seis", Toast.LENGTH_SHORT).show();
        } else {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                String id = user.getUid();
                                User user1 = new User(id, name, phone, email);
                                referenceUser.child(user.getUid()).setValue(user1);
                                Toast.makeText(RegisterActivity.this, "Usuario cadastrado com sucesso!", Toast.LENGTH_SHORT).show();

                                //registerUser();
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });


        }


    }

   /* private void registerUser() {

        String email = this.mViewHolder.editRegisterAddEmail.getText().toString();
        String password = this.mViewHolder.editRegisterAddPassword.getText().toString();


    }*/


    /* public void showDialog() {
       Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_negative);
        textView.setText("text");
        dialog.show();

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_negative, null);

        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }*/

    public static class ViewHolder {

        TextInputEditText editRegisterAddName, editRegisterAddEmail, editRegisterAddPhone, editRegisterAddPassword;
        Button submit, cancel;


    }
}