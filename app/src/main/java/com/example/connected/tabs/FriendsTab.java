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
import com.example.connected.activities.MessageViewActivity;

public class FriendsTab extends Fragment {
    View friendsView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        this.friendsView = inflater.inflate(R.layout.friends_fragment, container, false);

        ListView friendsListView = friendsView.findViewById(R.id.groupsListView);
        FriendsListAdapter friendsListAdapter = new FriendsListAdapter(friendsView.getContext());
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
}
