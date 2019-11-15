package com.example.connected.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.connected.DB.User;
import com.example.connected.R;
import com.theophrast.ui.widget.SquareImageView;

import java.util.ArrayList;
import java.util.List;

public class FriendsListAdapter extends BaseAdapter {
    LayoutInflater mInflater = null;
    List<User> users = new ArrayList<>();

    private TextView usernameTextView;
    private TextView ageTextView;
    private TextView statusTextView;
    private TextView statusIcon;
    private SquareImageView userImage;

    public FriendsListAdapter(Context c) {
        users.add(new User("krystian", 23, false, R.drawable.email_icon));
        users.add(new User("filip", 21, true, R.drawable.email_icon));
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void initializeViews(View v) {
        this.usernameTextView = v.findViewById(R.id.usernameTextView);
        this.ageTextView = v.findViewById(R.id.ageTextView);
        this.statusTextView = v.findViewById(R.id.statusTextView);
        this.statusIcon = v.findViewById(R.id.statusIcon);
        this.userImage = v.findViewById(R.id.userImageView);
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
        initializeViews(v);

        User currUser = users.get(position);
        usernameTextView.setText(currUser.getUsername());
        ageTextView.setText("age");
        userImage.setImageResource(R.drawable.avatar_icon);

        if(currUser.isActive()) {
            statusTextView.setText("ONLINE");
            statusIcon.setBackgroundResource(R.drawable.green_circle_drawable);
        }
        else {
            statusTextView.setText("OFFLINE");
            statusIcon.setBackgroundResource(R.drawable.red_circle_drawable);
        }

        return v;
    }
}
