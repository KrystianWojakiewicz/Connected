package com.example.connected.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    DatabaseReference rootRef;
    StorageReference firebaseStorageRef;

    EditText loginEditText;
    EditText passwordEditText;
    TextView alreadyHaveAccTextView;
    ProgressDialog progressDialog;
    Button registerBtn;


    private void initializeViews() {
        this.mAuth = FirebaseAuth.getInstance();
        this.rootRef = FirebaseDatabase.getInstance().getReference().getRoot();
        this.firebaseStorageRef = FirebaseStorage.getInstance().getReference();

        this.progressDialog = new ProgressDialog(this);

        this.loginEditText = findViewById(R.id.loginEditText);
        this.passwordEditText = findViewById(R.id.passwordEditText);
        this.alreadyHaveAccTextView = findViewById(R.id.alreadyHaveAccTextView);
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

        this.alreadyHaveAccTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });

    }
    private void createNewUser() {
        final String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(login)) {
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

            this.mAuth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {

                        initializeNewUser();

                        goToLoginActivity();
                        Toast.makeText(RegisterActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
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

    private void initializeNewUser() {
        final String currentUid = mAuth.getCurrentUser().getUid();
        final String usersKey = getString(R.string.Users);
        final String imageKey = getString(R.string.Image);
        final String imagesFolderKey = getString(R.string.ImagesFolder);
        final String defaultImageKey = getString(R.string.DefaultImage);


        firebaseStorageRef.child(imagesFolderKey).child(defaultImageKey).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                String defaultProfileImage = task.getResult().toString();

                rootRef.child(usersKey).child(currentUid).child(imageKey).setValue(defaultProfileImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Couldn't set up default Image", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void goToLoginActivity() {
        Intent loginScreenIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginScreenIntent);
    }
}
