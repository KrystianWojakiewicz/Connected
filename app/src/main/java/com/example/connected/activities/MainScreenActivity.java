package com.example.connected.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.connected.R;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.connected.tabs.pageView.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainScreenActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

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
        this.rootRef = FirebaseDatabase.getInstance().getReference();
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
            case R.id.find_user_option : {
                goToFindUsers();
                break;
            }

            case R.id.create_grp_option : {
                requestNewGroup();
                break;
            }

            case R.id.settings_option : {
                break;
            }

            case R.id.logout_option : {
                mAuth.signOut();
                goToLoginActivity();
                break;
            }

        }

        return true;
    }

    private void requestNewGroup() {
        final AlertDialog.Builder createGroupPopUp = new AlertDialog.Builder(this);
        createGroupPopUp.setTitle("Create New Group Chat");

        final EditText groupNameEditText = new EditText(this);
        groupNameEditText.setHint("e.g Programming Team");
        createGroupPopUp.setView(groupNameEditText);

        createGroupPopUp.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameEditText.getText().toString();
                if(TextUtils.isEmpty(groupName)) {
                    Toast.makeText(MainScreenActivity.this, "Please Enter a Valid Group Name", Toast.LENGTH_SHORT).show();
                }
                else {
                    createNewGroup(groupName);
                    dialog.dismiss();
                }
            }
        });

        createGroupPopUp.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                     dialog.cancel();
            }
        });

        createGroupPopUp.show();
    }

    private void createNewGroup(String groupName) {
        rootRef.child("Groups").child(groupName).setValue("");
    }

    private void goToFindUsers() {
        Intent goToFindUsersIntent = new Intent(getApplicationContext(), FindUsersActivity.class);
        startActivity(goToFindUsersIntent);
    }

    private void goToLoginActivity() {
        Intent loginScreenIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginScreenIntent);
        finish();
    }
}
