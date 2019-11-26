package com.example.connected.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.connected.DB.Contact;
import com.example.connected.R;
import com.example.connected.activities.MessageViewActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FriendsTab extends Fragment {
    View friendsView;
    ArrayList<Contact> friends = new ArrayList<>();
    FriendsListAdapter myAdapter;

    DatabaseReference rootRef;
    DatabaseReference usersRef;
    FirebaseUser currUser;
    String currUid;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        this.friendsView = inflater.inflate(R.layout.friends_fragment, container, false);

        rootRef = FirebaseDatabase.getInstance().getReference();
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        currUid = currUser.getUid();
        usersRef = rootRef.child(getString(R.string.Users));


        ListView friendsListView = friendsView.findViewById(R.id.groupsListView);
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter(friendsView.getContext(), this.friends);
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent messageViewIntent = new Intent(friendsView.getContext(), MessageViewActivity.class);
                startActivity(messageViewIntent);
            }
        });

        friendsListView.setAdapter(friendsListAdapter);
        return friendsView;
    }

    private void requestFriendsFromDatabase() {
        rootRef.child(getString(R.string.Users)).child(currUid).child(getString(R.string.Friends)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<Contact> set = new HashSet<>();

                while (iterator.hasNext()) {
                    DataSnapshot nextFriend = ((DataSnapshot)iterator.next());
                    String name = nextFriend.child("name").getValue().toString();
                    String image = nextFriend.child("image").getValue().toString();
                    set.add(new Contact(name, image, "ONLINE"));
                }
                friends.clear();
                friends.addAll(set);
//                arrayAdapter.notifyDataSetChanged();
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
