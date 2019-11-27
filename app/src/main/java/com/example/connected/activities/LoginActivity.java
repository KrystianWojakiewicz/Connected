package com.example.connected.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
//        loginScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginScreenIntent);
//        finish();
    }
//
//    private class QueryLogin extends AsyncTask<String,String,String> {
//        String login;
//        String password;
//        String msg = "";
//
//        ResultSet res = null;
//
//        // JDBC driver name
//        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
//
//        // Database URL
//        static final String DB_URL = "jdbc:mysql://" +
//                DbStrings.DATABASE_URL + "/" +
//                DbStrings.DATABASE_NAME;
//
//
//        QueryLogin(String login, String password) {
//            this.login = login;
//            this.password = password;
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            Connection conn = null;
//            Statement statement = null;
//
//            try {
//                Class.forName(JDBC_DRIVER);
//                conn = DriverManager.getConnection(DB_URL, DbStrings.USERNAME, DbStrings.PASSWORD);
//                statement = conn.createStatement();
//                String sql = "SELECT * FROM USERS WHERE Login='" + login + "'" +
//                        "AND Password='" + password + "'";
//                res = statement.executeQuery(sql);
//
//            }
//            catch (SQLException connError) {
//                msg = "An Exception was thrown for JDBC";
//                connError.printStackTrace();
//
//            }
//            catch (ClassNotFoundException e) {
//                msg = "A class not found exception was thrown";
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            try {
//                if (res.first()) {
//                    Intent mainScreenIntent = new Intent(getApplicationContext(), MainScreenActivity.class);
//                    startActivity(mainScreenIntent);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
} // End of LoginActivity
