package com.example.connected.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.connected.R;
import com.example.connected.activities.MainScreenActivity;

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
                EditText numEditText = (EditText) findViewById(R.id.textNum);
                EditText num2EditText = (EditText) findViewById(R.id.textNum2);

                Intent mainScreenIntent = new Intent(getApplicationContext(), MainScreenActivity.class);
                startActivity(mainScreenIntent);
            }
        });
    }
}
