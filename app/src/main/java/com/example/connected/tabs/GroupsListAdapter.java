package com.example.connected.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.connected.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class GroupsListAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    View v;
    DatabaseReference rootRef;
    List<String> groups;
    TextView groupNameTextView;

    public GroupsListAdapter(Context c, List<String> groups) {
        this.groups = groups;
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.v = this.mInflater.inflate(R.layout.groups_list_layout, null);
        this.groupNameTextView = v.findViewById(R.id.groupNameTextView);
    }

    @Override
    public int getCount() {
        System.out.println("SIZE: " + groups.size());
        return groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.groupNameTextView.setText(groups.get(position));
        System.out.println("GROUP: " + groups.get(position));
        return v;
    }
}
