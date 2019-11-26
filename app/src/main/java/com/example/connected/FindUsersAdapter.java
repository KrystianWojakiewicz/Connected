package com.example.connected;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class FindUsersAdapter extends BaseAdapter {
    LayoutInflater mInflater;


    public FindUsersAdapter(Context c) {
            mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.find_friends_list_layout, null);

        Button friendsRequrstBtn = v.findViewById(R.id.friendRequestButton);
        friendsRequrstBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("sent friend request in adapter");
                sendFriendRequest();
            }
        });

        return v;
    }

    void sendFriendRequest() {

    }
}
