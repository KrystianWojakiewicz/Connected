package com.example.connected.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.connected.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.connected.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseUser;

public class MainScreenActivity extends AppCompatActivity {

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String google = "http://www.google.com";
                Uri uri = Uri.parse(google);

                Intent searchEngineIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(searchEngineIntent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {
            goToLoginActivity();
        }
    }

    private void goToLoginActivity() {
        Intent loginScreenIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginScreenIntent);
    }
}