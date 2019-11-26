package com.example.connected.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.connected.DB.Contact;
import com.example.connected.DB.User;
import com.example.connected.R;
import com.squareup.picasso.Picasso;
import com.theophrast.ui.widget.SquareImageView;

import java.util.ArrayList;
import java.util.List;

public class FriendsListAdapter extends BaseAdapter {
    LayoutInflater mInflater = null;
    List<Contact> friends = new ArrayList<>();

    private TextView usernameTextView;
    private TextView statusTextView;
    private TextView statusIcon;
    private SquareImageView userImageView;

    public FriendsListAdapter(Context c, ArrayList<Contact> friends) {
//        users.add(new User("krystian", false, "ICON"));
//        users.add(new User("filip", true, "ICON"));
        this.friends = friends;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private void initializeViews(View v) {
        this.usernameTextView = v.findViewById(R.id.usernameTextView);
        this.statusTextView = v.findViewById(R.id.statusTextView);
        this.statusIcon = v.findViewById(R.id.statusIcon);
        this.userImageView = v.findViewById(R.id.userImageView);
    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.users_list_layout, null);
        initializeViews(v);

        Contact currUser = friends.get(position);
        usernameTextView.setText(currUser.getName());

        userImageView.setImageResource(R.drawable.default_avatar);
        Picasso.get().load(this.friends.get(position).getImage()).into(this.userImageView);

//        statusTextView.setText(currUser.isActive());
        statusIcon.setBackgroundResource(R.drawable.green_circle_drawable);
//      statusIcon.setBackgroundResource(R.drawable.red_circle_drawable);

        return v;
    }
}
