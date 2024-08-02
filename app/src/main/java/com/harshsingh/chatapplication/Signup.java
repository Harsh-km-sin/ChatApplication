package com.harshsingh.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Signup extends AppCompatActivity {

    EditText edtEmail, edtPassword, edtName;
    Button btnSignup;

    FirebaseAuth mAuth;
    DatabaseReference mDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Objects.requireNonNull(getSupportActionBar()).hide();

        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email_signup);
        edtPassword = findViewById(R.id.edt_password_signup);

        btnSignup = findViewById(R.id.btn_signup);
        mAuth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Signup.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    signup(name, email, password);
                }
            }
        });
    }

    private void signup(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserToDatabase(name, email, Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                            Intent intent = new Intent(Signup.this, MainActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(Signup.this, "Some Error Occurred: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addUserToDatabase(String name, String email, String uid) {
        mDbRef = FirebaseDatabase.getInstance().getReference();
        mDbRef.child("Users").child(uid).setValue(new User(name,email,uid));
    }
}
