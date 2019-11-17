package com.example.connected.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.connected.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private StorageReference firebaseStorageRef;
    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private String currentUid;

    private Button saveBtn;
    private EditText nameEditText;
    private EditText statusEditext;
    private ImageView userImageView;
    private Toolbar myToolbar;
    private KeyListener nameEditListener;


    private void initializeView() {
        this.firebaseStorageRef = FirebaseStorage.getInstance().getReference();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUid = this.mAuth.getCurrentUser().getUid();

        saveBtn = findViewById(R.id.saveBtn);
        nameEditText = findViewById(R.id.nameEditText);
        statusEditext = findViewById(R.id.statusEditText);
        userImageView = findViewById(R.id.userImageView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeView();

        myToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Profile Settings");

        nameEditListener = nameEditText.getKeyListener();
        nameEditText.setKeyListener(null);

        retrieveDataFromDatabase();

        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
    }

    private void updateUserInfo() {
        String name = nameEditText.getText().toString();
        String status = statusEditext.getText().toString();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(SettingsActivity.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(status)) {
            Toast.makeText(SettingsActivity.this, "Enter a valid status", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> map = new HashMap<>();
            map.put(getString(R.string.Uid), currentUid);
            map.put(getString(R.string.Name), name);
            map.put(getString(R.string.Status), status);
            rootRef.child(getString(R.string.Users)).child(currentUid).updateChildren(map);
        }
    }

    private void retrieveDataFromDatabase() {
        rootRef.child(getString(R.string.Users)).child(this.currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nameKey = getString(R.string.Name);
                String statusKey = getString(R.string.Status);
                String imageKey = getString(R.string.Image);

                DataSnapshot nameSnapshot = dataSnapshot.child(nameKey);
                DataSnapshot statusSnapshot = dataSnapshot.child(statusKey);
                DataSnapshot imageSnapshot = dataSnapshot.child(imageKey);

                if(dataSnapshot.exists() && nameSnapshot.exists() && statusSnapshot.exists()) {
                    String name = nameSnapshot.getValue().toString();
                    String status = statusSnapshot.getValue().toString();
                    String imageUri = imageSnapshot.getValue().toString();

                    nameEditText.setText(name);
                    statusEditext.setText(status);
                    Picasso.get().load(imageUri).into(userImageView);
                }
                else if(dataSnapshot.exists() && nameSnapshot.exists()) {
                    String name = nameSnapshot.getValue().toString();
                    String imageUri = imageSnapshot.getValue().toString();

                    nameEditText.setText(name);
                    Picasso.get().load(imageUri).into(userImageView);
                }
                else {
                    String imageUri = imageSnapshot.getValue().toString();
                    Picasso.get().load(imageUri).into(userImageView);

                    nameEditText.setKeyListener(nameEditListener);
                    Toast.makeText(SettingsActivity.this, "Please Let us know who you are", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
