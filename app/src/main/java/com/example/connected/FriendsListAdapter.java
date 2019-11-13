package com.example.connected;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FriendsListAdapter extends BaseAdapter {
    LayoutInflater mInflater = null;
    List<User> users = new ArrayList<>();

    public FriendsListAdapter(Context c) {
        users.add(new User("krystian", 23, false));
        users.add(new User("filip", 21, true));
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View v = mInflater.inflate(R.layout.friends_list_layout, null);
        TextView usernameTextView = v.findViewById(R.id.usernameTextView);
        TextView ageTextView = v.findViewById(R.id.ageTextView);
        TextView statusTextView = v.findViewById(R.id.statusTextView);

        User currUser = users.get(position);
        usernameTextView.setText(currUser.getUsername());
        ageTextView.setText("age");
        if(currUser.isActive()) {
            statusTextView.setText("ONLINE");
        }
        else {
            statusTextView.setText("OFFLINE");
        }

        return v;
    }
}
