package com.example.connected.activities.find_users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ListView;

import com.example.connected.DB.User;
import com.example.connected.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FindUsersActivity extends AppCompatActivity {

    private ListView foundUsersListView;
    private Toolbar myToolbar;
    private FindUsersAdapter myAdapter;
    private ArrayList<User> users = new ArrayList<>();


    private DatabaseReference rootRef;
    private String currentUid;
    private FirebaseAuth mAuth;

    private void initializeViews() {
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUid = mAuth.getCurrentUser().getUid();

        this.foundUsersListView = findViewById(R.id.findUsersListView);
        myAdapter = new FindUsersAdapter(this, users);
        foundUsersListView.setAdapter(myAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        initializeViews();

        this.myToolbar = findViewById(R.id.find_user_toolbar);
        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setTitle("Find User");

        requestUsersFromDatabase();
    }

    private void requestUsersFromDatabase() {
        rootRef.child(getString(R.string.Users)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<User> set = new HashSet<>();

                while (iterator.hasNext()) {
                    DataSnapshot nextUser = ((DataSnapshot)iterator.next());
                    Object uidObject = nextUser.child("uid").getValue();
                    Object nameObject = nextUser.child("name").getValue();

                    if(uidObject.toString().equals(currentUid)) {
                        continue;
                    }

                    if (nameObject != null) {
                        String name = nextUser.child("name").getValue().toString();
                        String image = nextUser.child("image").getValue().toString();
                        String status = nextUser.child("status").getValue().toString();
                        String uid = nextUser.child("uid").getValue().toString();

                        set.add(new User(name, image,status, uid));
                    }
                }
                users.clear();
                users.addAll(set);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
