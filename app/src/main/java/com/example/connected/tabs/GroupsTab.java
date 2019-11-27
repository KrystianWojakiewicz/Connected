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

import com.example.connected.DB.Group;
import com.example.connected.R;
import com.example.connected.activities.GroupChatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GroupsTab extends Fragment {
    DatabaseReference rootRef;

    private ArrayList<Group> groups = new ArrayList<>();
    private ListView groupsListView;
    private View groupsView;;
    private GroupsListAdapter myGroupsAdapter;
//    private ArrayAdapter<String> arrayAdapter;

    private void initializeViews() {
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.groupsListView = this.groupsView.findViewById(R.id.groupsListView);
        this.myGroupsAdapter = new GroupsListAdapter(this.groupsView.getContext(), this.groups);
        this.groupsListView.setAdapter(this.myGroupsAdapter);
//        this.arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.groups_list_layout_old, groups);
//        this.groupsListView.setAdapter(this.arrayAdapter);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        this.groupsView = inflater.inflate(R.layout.groups_fragment, container, false);

        initializeViews();
        requestGroupsFromDatabase();

        this.groupsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentGroupName = ((Group)parent.getItemAtPosition(position)).getName();

                Intent goToGroupChatIntent = new Intent(groupsView.getContext(), GroupChatActivity.class);
                goToGroupChatIntent.putExtra("groupName", currentGroupName);
                startActivity(goToGroupChatIntent);
            }
        });

        return groupsView;
    }

    private void requestGroupsFromDatabase() {
        rootRef.child(getString(R.string.Groups)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<Group> set = new HashSet<>();

                while (iterator.hasNext()) {
                    DataSnapshot nextGroup = ((DataSnapshot)iterator.next());
                    if(nextGroup.child("image").getValue() != null) {
                        String itGroup = nextGroup.getKey();
                        String imageUri = nextGroup.child("image").getValue().toString();
                        set.add(new Group(itGroup, imageUri));
                    }
                }
                groups.clear();
                groups.addAll(set);
//                arrayAdapter.notifyDataSetChanged();
                myGroupsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
