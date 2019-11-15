package com.example.connected.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.connected.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class GroupsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private View v;
    private DatabaseReference rootRef;
    private ArrayList<String> groups;
    private TextView groupNameTextView;

    public GroupsListAdapter(Context c, ArrayList<String> groups) {
        this.groups = groups;
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.groups.size();
    }

    @Override
    public Object getItem(int position) {
        return this.groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        this.v = this.mInflater.inflate(R.layout.groups_list_layout, null);
        this.groupNameTextView = v.findViewById(R.id.groupNameTextView);
        this.groupNameTextView.setText(this.groups.get(position));
        return v;
    }
}
