package com.example.connected.tabs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connected.DB.Contact;
import com.example.connected.R;
import com.example.connected.activities.SettingsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.theophrast.ui.widget.SquareImageView;

import java.util.ArrayList;


public class FriendsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Contact> friends;

    private Context context;

    private TextView usernameTextView;
//    private TextView statusIcon;
    private SquareImageView userImageView;

    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private DatabaseReference rootRef;


    public FriendsListAdapter(Context c, ArrayList<Contact> friends) {
        this.friends = friends;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = c;
    }

    private void initializeViews(View v) {
        this.usernameTextView = v.findViewById(R.id.usernameTextView);
//        this.statusIcon = v.findViewById(R.id.statusIcon);
        this.userImageView = v.findViewById(R.id.userImageView);

        mAuth = FirebaseAuth.getInstance();
        currUser = mAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getCount() {
        return this.friends.size();
    }

    @Override
    public Object getItem(int position) {
        return this.friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.users_list_layout, null);
        initializeViews(v);

        final Contact currUserEntry = friends.get(position);
        usernameTextView.setText(currUserEntry.getName());

        userImageView.setImageResource(R.drawable.default_avatar);
        Picasso.get().load(this.friends.get(position).getImage()).into(this.userImageView);

//        statusIcon.setBackgroundResource(R.drawable.green_circle_drawable);

        Button viewProfileBtn = v.findViewById(R.id.viewProfileButton);
        viewProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSettingsActivity(currUserEntry);
            }
        });

        Button unfriendButton = v.findViewById(R.id.unfriendButton);
        unfriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFriend(currUserEntry);
            }
        });

        return v;
    }

    private void goToSettingsActivity(Contact currUserEntry) {
        Intent goToSettingsIntent = new Intent(context, SettingsActivity.class);
        goToSettingsIntent.putExtra("uid", currUserEntry.getUid());
        goToSettingsIntent.putExtra("editable", false);
        context.startActivity(goToSettingsIntent);
    }

    private void deleteFriend(Contact currUserEntry) {
        DatabaseReference friendToDelete = rootRef.child("users").child(currUser.getUid()).child("friends").child(currUserEntry.getName());
        friendToDelete.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Friend Deleted Successfully", Toast.LENGTH_LONG).show();
            }
        });
    }
}
