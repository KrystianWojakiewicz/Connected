package com.example.connected.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.connected.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainScreenActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private StorageReference imagesStorageRef;

    private SectionsPagerAdapter mySectionsPagerAdapter;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private Toolbar myToolbar;

    private void initializeViews() {
        this.myToolbar = findViewById(R.id.main_screen_toolbar);
        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        this.myViewPager = findViewById(R.id.view_pager);
        this.mySectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        this.myViewPager.setAdapter(mySectionsPagerAdapter);

        this.myTabLayout = findViewById(R.id.myTabLayout);
        this.myTabLayout.setupWithViewPager(myViewPager);

        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = this.mAuth.getCurrentUser();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.imagesStorageRef = FirebaseStorage.getInstance().getReference().child(getString(R.string.ImagesFolder));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        initializeViews();
        verifyIfUserExists();
    }

    private void verifyIfUserExists() {
        if(currentUser == null) { return; }


        String currentUid = currentUser.getUid();
        this.rootRef.child(getString(R.string.Users)).child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child(getString(R.string.Name)).exists()) {
                    goToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser == null) {
            goToLoginActivity();
        }
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
                goToFindUsersActivity();
                break;
            }

            case R.id.create_grp_option : {
                requestNewGroup();
                break;
            }

            case R.id.settings_option : {
                goToSettingsActivity();
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

    private void createNewGroup(final String groupName) {
        final DatabaseReference newGroupRef = this.rootRef.child(getString(R.string.Groups)).child(groupName);

        newGroupRef.setValue("");
        newGroupRef.child(getString(R.string.Messages)).setValue("");
        newGroupRef.child(getString(R.string.Members)).setValue("");
        this.imagesStorageRef.child(getString(R.string.DefaultChatImage)).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    Uri chatImageUri = task.getResult();

                    newGroupRef.child(getString(R.string.Image)).setValue(chatImageUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(MainScreenActivity.this, "Group Created Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void goToFindUsersActivity() {
        Intent goToFindUsersIntent = new Intent(getApplicationContext(), FindUsersActivity.class);
        startActivity(goToFindUsersIntent);
    }


    private void goToSettingsActivity() {
        Intent goToSettingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(goToSettingsIntent);
    }

    private void goToLoginActivity() {
        Intent loginScreenIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginScreenIntent);
//        finish();
    }
}
