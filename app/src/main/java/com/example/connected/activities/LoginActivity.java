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
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private TextView dontHaveAccTextView;
    private ProgressDialog progressDialog;


    private void initializeViews() {
        this.dontHaveAccTextView = findViewById(R.id.dontHaveAccTextView);
        this.loginBtn = findViewById(R.id.loginBtn);
        this.loginEditText = findViewById(R.id.loginEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);
        this.progressDialog = new ProgressDialog(this);

        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = this.mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeViews();

        this.dontHaveAccTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRegisterActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(goToRegisterActivity);
            }
        });

        this.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        String username = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Please Enter Your Username In The Specified Field", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please Enter Your Username In The Specified Field", Toast.LENGTH_SHORT).show();
        }
        else {
            this.progressDialog.setTitle("Logging in");
            this.progressDialog.setMessage("Please Wait...");
            this.progressDialog.setCanceledOnTouchOutside(true);
            this.progressDialog.show();

            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        goToMainScreenActivity();
                        Toast.makeText(LoginActivity.this, "Signed In Successfully :)", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else {
                        String msg = task.getException().toString();
                        Toast.makeText(LoginActivity.this, "ERROR: " + msg, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    private void goToMainScreenActivity() {
        Intent loginScreenIntent = new Intent(getApplicationContext(), MainScreenActivity.class);
        loginScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginScreenIntent);
        finish();
    }
} // End of LoginActivity
