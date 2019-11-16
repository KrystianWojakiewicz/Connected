package com.example.connected.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connected.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;


public class GroupChatActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private ImageButton sendMsgButton;
    private ScrollView groupChatScrollView;
    private TextView chatMsgDisplay;
    private EditText messageEditText;


    private DatabaseReference groupNameRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private void initializeViews() {
        this.myToolbar = findViewById(R.id.group_chat_toolbar);
        this.sendMsgButton = findViewById(R.id.sendMsgButton);
        this.sendMsgButton = findViewById(R.id.sendMsgButton);
        this.messageEditText = findViewById(R.id.messageEditText);
        this.groupChatScrollView = findViewById(R.id.groupChatScrollView);
        this.chatMsgDisplay = findViewById(R.id.msgDisplay);


        setSupportActionBar(this.myToolbar);
        String grpName = getIntent().getExtras().get("groupName").toString();
        getSupportActionBar().setTitle(grpName);

        this.groupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(grpName);
        this.mAuth = FirebaseAuth.getInstance();
        this.currentUser = this.mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        initializeViews();
        this.sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageInDatabase();
                messageEditText.setText("");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        this.groupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    updateMessageDisplay(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    updateMessageDisplay(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveMessageInDatabase() {
        String message = messageEditText.getText().toString();
    
        if(TextUtils.isEmpty(message)) {
            Toast.makeText(this, "Please enter a message before sending", Toast.LENGTH_SHORT).show();
        }
        else {
            DatabaseReference messageKeyRef = groupNameRef.push();

            Calendar calDate = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
            String date = dateFormat.format(calDate.getTime());

            Calendar calTime = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String time = timeFormat.format(calTime.getTime());

            HashMap<String, Object> messageToSend = new HashMap<>();

            System.out.println("NAME: " + this.currentUser.getDisplayName());
            messageToSend.put("Name", this.currentUser.getEmail());
            messageToSend.put("Date", date);
            messageToSend.put("Time", time);
            messageToSend.put("Msg", message);
            messageKeyRef.updateChildren(messageToSend);
        }
    }

    private void updateMessageDisplay(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext()) {
            String date = (String) ((DataSnapshot)iterator.next()).getValue();
            String msg = (String) ((DataSnapshot)iterator.next()).getValue();
            String name = (String) ((DataSnapshot)iterator.next()).getValue();
            String time = (String) ((DataSnapshot)iterator.next()).getValue();

            this.chatMsgDisplay.append(name + ":\n" + date + "  " + time + "\n\n" + msg + "\n\n\n");
            this.groupChatScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}
