package com.example.connected.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.connected.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;

import com.example.connected.tabs.pageView.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainScreenActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    private SectionsPagerAdapter mySectionsPagerAdapter;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private Toolbar myToolbar;

    private void initializeViews() {
        this.myToolbar = findViewById(R.id.main_screen_toolbar);
        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setTitle("Echo");

        this.myViewPager = findViewById(R.id.view_pager);
        this.mySectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        this.myViewPager.setAdapter(mySectionsPagerAdapter);

        this.myTabLayout = findViewById(R.id.myTabLayout);
        this.myTabLayout.setupWithViewPager(myViewPager);

        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = this.mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        initializeViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.settings_option :
                break;
            case R.id.logout_option :
                mAuth.signOut();
                goToLoginActivity();
                break;
        }

        return true;
    }

    private void goToLoginActivity() {
        Intent loginScreenIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginScreenIntent);
    }
}
