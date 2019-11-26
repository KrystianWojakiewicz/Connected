package com.example.connected.tabs;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.connected.DB.Group;
import com.example.connected.R;
import com.example.connected.activities.SettingsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GroupsListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private View v;
    private DatabaseReference rootRef;
    private ArrayList<Group> groups;
    private TextView groupNameTextView;
    private ImageView groupImage;

    private StorageReference imagesStorageRef;

    public GroupsListAdapter(Context c, ArrayList<Group> groups) {
        this.groups = groups;
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.imagesStorageRef = FirebaseStorage.getInstance().getReference().child(c.getString(R.string.ImagesFolder));

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
        this.groupNameTextView.setText(this.groups.get(position).getName());

        this.groupImage = v.findViewById(R.id.groupImageView);
        Picasso.get().load(this.groups.get(position).getImage()).into(this.groupImage);

        groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Button pressed in adapter", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }
}
