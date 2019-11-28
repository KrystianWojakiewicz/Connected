package com.example.connected.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.connected.DB.Contact;
import com.example.connected.DB.Group;
import com.example.connected.FindUsersAdapter;
import com.example.connected.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    private ArrayList<Contact> users = new ArrayList<>();


    private FirebaseRecyclerAdapter<Contact, FindUserRecyclerViewHolder> recyclerAdapter;
    private FirebaseRecyclerOptions<Contact> recyclerOptions;
    private DatabaseReference usersRoot;
    private DatabaseReference rootRef;
    private String currentUid;
    private String currentUsername;
    private FirebaseAuth mAuth;

    private void initializeViews() {
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.usersRoot = rootRef.child(getString(R.string.Users));
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUid = mAuth.getCurrentUser().getUid();

        this.foundUsersListView = findViewById(R.id.findUsersListView);
//        this.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                Set<Contact> set = new HashSet<>();

                while (iterator.hasNext()) {
                    DataSnapshot nextUser = ((DataSnapshot)iterator.next());
                    Object uidObject = nextUser.child("uid").getValue();
                    Object nameObject = nextUser.child("name").getValue();

                    if ( nameObject != null && !uidObject.toString().equals(currentUid)) {
                        String name = nextUser.child("name").getValue().toString();
                        String image = nextUser.child("image").getValue().toString();
                        set.add(new Contact(name, image,""));
                    }

                }
                users.clear();
                users.addAll(set);
//                arrayAdapter.notifyDataSetChanged();
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        this.recyclerOptions = new FirebaseRecyclerOptions.Builder<Contact>()
//                .setQuery(this.usersRoot, Contact.class)
//                .build();
//
//        this.recyclerAdapter = new FirebaseRecyclerAdapter<Contact, FindUserRecyclerViewHolder>(recyclerOptions) {
//            @Override
//            protected void onBindViewHolder(@NonNull FindUserRecyclerViewHolder holder, int position, @NonNull Contact model) {
//                holder.username.setText(model.getName());
//                holder.statusTextView.setText(model.getStatus());
//            }
//
//            @NonNull
//            @Override
//            public FindUserRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View v = getLayoutInflater().from(parent.getContext()).inflate(R.layout.users_list_layout, parent, false);
//                FindUserRecyclerViewHolder viewHolder = new FindUserRecyclerViewHolder(v);
//
//                return viewHolder;
//            }
//        };
//
//        this.myRecyclerView.setAdapter(recyclerAdapter);
//        this.recyclerAdapter.startListening();
//    }

    public static class FindUserRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView username;
//        TextView isActive;
        TextView statusTextView;

        public FindUserRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            this.username = itemView.findViewById(R.id.usernameTextView);
            this.statusTextView = itemView.findViewById(R.id.statusTextView);
//            this.isActive = itemView.findViewById(R.id.statusIcon);
        }
    }
}
