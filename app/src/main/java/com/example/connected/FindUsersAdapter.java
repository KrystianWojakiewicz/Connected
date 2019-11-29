package com.example.connected;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connected.DB.Contact;
import com.example.connected.activities.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class FindUsersAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Contact> users;

    private Context context;

    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private DatabaseReference rootRef;

    public FindUsersAdapter(Context c, ArrayList<Contact> users) {
        this.users = users;
        this.context = c;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.find_friends_list_layout, null);

        TextView usernameTextView = v.findViewById(R.id.usernameTextView);
        ImageView userImage = v.findViewById(R.id.userImageView);

        final Contact currUserEntry = users.get(position);
        usernameTextView.setText(currUserEntry.getName());
        Picasso.get().load(currUserEntry.getImage()).into(userImage);

        Button friendsRequestBtn = v.findViewById(R.id.friendRequestButton);
        friendsRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest(currUserEntry);
            }
        });

        Button viewProfileBtn = v.findViewById(R.id.viewProfileButton);
        viewProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettingsActivity(currUserEntry);
            }
        });


        return v;
    }

    void sendFriendRequest(Contact currUserEntry) {
        String currUid = currUser.getUid();
        final DatabaseReference currFriendRef = rootRef.child("users").child(currUid).child("friends").child(currUserEntry.getName());

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", currUserEntry.getName());
        map.put("image", currUserEntry.getImage());
        map.put("status", currUserEntry.getStatus());
        map.put("uid", currUserEntry.getUid());

        currFriendRef.updateChildren(map);
    }

    private void goToSettingsActivity(Contact currUserEntry) {
        Intent goToSettingsIntent = new Intent(context, SettingsActivity.class);
        goToSettingsIntent.putExtra("uid", currUserEntry.getUid());
        goToSettingsIntent.putExtra("editable", false);
        context.startActivity(goToSettingsIntent);
    }
}
