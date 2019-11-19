package com.example.connected.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.connected.DB.Contact;
import com.example.connected.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindUsersActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private Toolbar myToolbar;

    private FirebaseRecyclerAdapter<Contact, FindUserRecyclerViewHolder> recyclerAdapter;
    private FirebaseRecyclerOptions<Contact> recyclerOptions;
    private DatabaseReference usersRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        this.myRecyclerView = findViewById(R.id.myRecyclerView);
        this.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.myToolbar = findViewById(R.id.find_user_toolbar);
        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setTitle("Find User");

        this.usersRoot = FirebaseDatabase.getInstance().getReference().child(getString(R.string.Users));
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.recyclerOptions = new FirebaseRecyclerOptions.Builder<Contact>()
                .setQuery(this.usersRoot, Contact.class)
                .build();

        this.recyclerAdapter = new FirebaseRecyclerAdapter<Contact, FindUserRecyclerViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FindUserRecyclerViewHolder holder, int position, @NonNull Contact model) {
                holder.username.setText(model.getName());
                holder.statusTextView.setText(model.getStatus());
            }

            @NonNull
            @Override
            public FindUserRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = getLayoutInflater().from(parent.getContext()).inflate(R.layout.users_list_layout, parent, false);
                FindUserRecyclerViewHolder viewHolder = new FindUserRecyclerViewHolder(v);

                return viewHolder;
            }
        };

        this.myRecyclerView.setAdapter(recyclerAdapter);
        this.recyclerAdapter.startListening();
    }

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
