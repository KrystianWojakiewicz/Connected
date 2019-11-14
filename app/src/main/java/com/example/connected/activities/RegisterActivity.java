package com.example.connected.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connected.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText loginEditText;
    EditText passwordEditText;
    ProgressDialog progressDialog;
    Button registerBtn;

    private void initializeViews() {
        this.mAuth = FirebaseAuth.getInstance();
        this.progressDialog = new ProgressDialog(this);

        this.loginEditText = findViewById(R.id.loginEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);

        this.registerBtn = findViewById(R.id.registerBtn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeViews();

        this.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewUser();
            }
        });

    }
    private void createNewUser() {
        String username = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please Enter Your Username In The Specified Field", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Your Username In The Specified Field", Toast.LENGTH_SHORT).show();
        }
        else {
            this.progressDialog.setTitle("Creating new Account");
            this.progressDialog.setMessage("Please Wait...");
            this.progressDialog.setCanceledOnTouchOutside(true);
            this.progressDialog.show();

            mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        goToLoginActivity();
                        Toast.makeText(RegisterActivity.this, "Acount Created Successfully", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else {
                        String msg = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, "ERROR: " + msg, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void goToLoginActivity() {
        Intent loginScreenIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginScreenIntent);
    }
}
