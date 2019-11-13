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

import com.example.connected.R;
import com.example.connected.DB.User;
import com.example.connected.activities.MessageViewActivity;

public class FriendsTab extends Fragment {
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.friends_tab, container, false);

        ListView friendsListView = v.findViewById(R.id.friendsListView);
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter(v.getContext());
        friendsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent messageViewIntent = new Intent(v.getContext(), MessageViewActivity.class);
                User a = (User)parent.getItemAtPosition(position);
                messageViewIntent.putExtra("clickedUser", a.getUsername());
                startActivity(messageViewIntent);
            }
        });

        friendsListView.setAdapter(friendsListAdapter);
        return v;
    }
}
