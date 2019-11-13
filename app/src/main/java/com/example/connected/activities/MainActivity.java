package com.example.connected.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.connected.DbStrings;
import com.example.connected.R;
import com.example.connected.activities.MainScreenActivity;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addBtn = (Button)findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText loginEditText = (EditText) findViewById(R.id.loginEditText);
                EditText passwordEditText = (EditText) findViewById(R.id.passwordEditText);

                String loginString = loginEditText.getText().toString();
                String passwordString = passwordEditText.getText().toString();

                QueryLogin queryLogin = new QueryLogin(loginString, passwordString);
                queryLogin.execute("");
//                Intent mainScreenIntent = new Intent(getApplicationContext(), MainScreenActivity.class);
//                startActivity(mainScreenIntent);
            }
        });
    }
    private class QueryLogin extends AsyncTask<String,String,String> {
        String login;
        String password;
        String msg = "";

        ResultSet res = null;

        // JDBC driver name
        static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

        // Database URL
        static final String DB_URL = "jdbc:mysql://" +
                DbStrings.DATABASE_URL + "/" +
                DbStrings.DATABASE_NAME;


        QueryLogin(String login, String password) {
            this.login = login;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection conn = null;
            Statement statement = null;

            try {
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, DbStrings.USERNAME, DbStrings.PASSWORD);
                statement = conn.createStatement();
                String sql = "SELECT * FROM USERS WHERE Login='" + login + "'" +
                        "AND Password='" + password + "'";
                res = statement.executeQuery(sql);

            }
            catch (SQLException connError) {
                msg = "An Exception was thrown for JDBC";
                connError.printStackTrace();

            }
            catch (ClassNotFoundException e) {
                msg = "A class not found exception was thrown";
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (res.first()) {
                    Intent mainScreenIntent = new Intent(getApplicationContext(), MainScreenActivity.class);
                    startActivity(mainScreenIntent);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
} // End of MainActivity
