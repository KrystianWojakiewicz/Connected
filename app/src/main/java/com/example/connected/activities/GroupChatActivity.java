package com.example.connected.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.connected.R;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private ImageButton sendMsgButton;
    private ScrollView groupChatScrollView;
    private TextView chatMsgDisplay;


    private void initializeViews() {
        this.myToolbar = findViewById(R.id.group_chat_toolbar);
        this.sendMsgButton = findViewById(R.id.sendMsgButton);

        setSupportActionBar(this.myToolbar);
        String grpName = getIntent().getExtras().get("groupName").toString();
        getSupportActionBar().setTitle(grpName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initializeViews();
    }
}
