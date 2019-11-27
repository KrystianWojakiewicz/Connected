package com.example.connected.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.connected.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
    private StorageReference firebaseStorageRef;
    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private String currentUid;

    private Button saveBtn;
    private EditText nameEditText;
    private EditText statusEditText;
    private ImageView userImageView;
    private Toolbar myToolbar;
    private KeyListener nameEditListener;
    private ProgressDialog loadingDialog;
    private Uri userImageUri;

    final int GALLERY_CODE = 1;

    private void initializeView() {
        this.firebaseStorageRef = FirebaseStorage.getInstance().getReference();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUid = this.mAuth.getCurrentUser().getUid();

        saveBtn = findViewById(R.id.saveBtn);
        nameEditText = findViewById(R.id.nameEditText);
        statusEditText = findViewById(R.id.statusEditText);
        userImageView = findViewById(R.id.userImageView);
        loadingDialog = new ProgressDialog(this);
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


        this.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        this.userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseImageIntent = new Intent();
                chooseImageIntent.setAction(Intent.ACTION_GET_CONTENT);
                chooseImageIntent.setType("image/*");
                startActivityForResult(chooseImageIntent, GALLERY_CODE);
            }
        });

        retrieveDataFromDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK) {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            try {
                userImageUri = result.getUri();
                userImageView.setImageURI(userImageUri);
            }
            catch (Exception e) {
                e.getStackTrace();
                System.out.println("Bad result from CropImage");
            }
        }
    }

    private void updateUser() {
        String name = nameEditText.getText().toString();
        String status = statusEditText.getText().toString();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(SettingsActivity.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(status)) {
            Toast.makeText(SettingsActivity.this, "Enter a valid status", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingDialog.setTitle("Updating Profile Info");
            loadingDialog.setMessage("Please Wait...");
            loadingDialog.setCanceledOnTouchOutside(true);
            loadingDialog.show();

            updateUserImage();
            updateUserInfo(name, status);
        }
    }

    private void updateUserImage() {
        if(userImageUri == null) { return; }

        final StorageReference imageRef = firebaseStorageRef.child(getString(R.string.ImagesFolder)).child(currentUid + ".image");
        imageRef.putFile(userImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "Image successfully uploaded to server", Toast.LENGTH_SHORT).show();

                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {

                                final String uploadedImageUri = task.getResult().toString();
                                rootRef.child(getString(R.string.Users)).child(currentUid).child(getString(R.string.Image)).setValue(uploadedImageUri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(!task.isSuccessful()) {
                                            Toast.makeText(SettingsActivity.this, "ERROR: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            else {
                                Log.i("Error", task.getException().toString());
                            }
                        }
                    });
                }
            }
        });


    }

    private void updateUserInfo(String name, String status) {
        HashMap<String, Object> map = new HashMap<>();
        map.put(getString(R.string.Uid), currentUid);
        map.put(getString(R.string.Name), name);
        map.put(getString(R.string.Status), status);

        rootRef.child(getString(R.string.Users)).child(currentUid).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "Account updated Successfully", Toast.LENGTH_SHORT).show();

//                    recreate();
                    loadingDialog.dismiss();
                }
                else {
                    Toast.makeText(SettingsActivity.this, "ERROR: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }
        });
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
                    statusEditText.setText(status);
                    Picasso.get().load(imageUri).into(userImageView);
                }
                else if(dataSnapshot.exists() && nameSnapshot.exists()) {
                    String name = nameSnapshot.getValue().toString();
                    String imageUri = imageSnapshot.getValue().toString();

                    nameEditText.setText(name);
                    Picasso.get().load(imageUri).into(userImageView);
                }
                else {
//                    String imageUri = imageSnapshot.getValue().toString();
//                    Picasso.get().load(imageUri).into(userImageView);

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
