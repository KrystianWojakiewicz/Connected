package com.example.connected.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.connected.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class GroupsTab extends Fragment {
    DatabaseReference rootRef;

    private ArrayList<String> groups = new ArrayList<>();
    private ListView groupsListView;
    private View groupsView;
    GroupsListAdapter myGroupsAdapter;
    private ArrayAdapter<String> arrayAdapter;

    private void initializeViews() {
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.groupsListView = this.groupsView.findViewById(R.id.groupsListView);
//        this.myGroupsAdapter = new GroupsListAdapter(this.groupsView.getContext(), this.groups);
//        this.groupsListView.setAdapter(this.myGroupsAdapter);
        this.arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.groups_list_layout, groups);
        this.groupsListView.setAdapter(this.arrayAdapter);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        this.groupsView = inflater.inflate(R.layout.groups_fragment, container, false);

        initializeViews();

        requestGroupsFromDatabase();

        return groupsView;
    }

    private void requestGroupsFromDatabase() {
        rootRef.child("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<String> set = new HashSet<>();

                while (iterator.hasNext()) {
                    String itGroup = ((DataSnapshot)iterator.next()).getKey();
                    System.out.println("HAHAHAHAHA: " + itGroup);
                    set.add(itGroup);
                }
                groups.clear();
                groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
