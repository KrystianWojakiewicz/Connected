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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class MessageViewActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private ImageButton sendMsgButton;
    private EditText messageEditText;
    private ScrollView privateChatScrollView;
    private TextView chatMsgDisplay;

    private DatabaseReference myPrivateMessagesRef;
    private DatabaseReference yourPrivateMessagesRef;
    private DatabaseReference rootRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference usersRoot;
    private String currentUid;


    private void initializeViews() {
        this.myToolbar = findViewById(R.id.private_chat_toolbar);
        this.sendMsgButton = findViewById(R.id.sendMsgButton);
        this.sendMsgButton = findViewById(R.id.sendMsgButton);
        this.messageEditText = findViewById(R.id.messageEditText);
        this.privateChatScrollView = findViewById(R.id.privateChatScrollView);
        this.chatMsgDisplay = findViewById(R.id.msgDisplay);

        this.mAuth = FirebaseAuth.getInstance();
        this.rootRef = FirebaseDatabase.getInstance().getReference();
        this.currentUser = this.mAuth.getCurrentUser();
        this.currentUid = currentUser.getUid();
        this.usersRoot = this.rootRef.child(getString(R.string.Users));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);

        initializeViews();

        String friendName = getIntent().getExtras().get("friendName").toString();
        String friendUid = getIntent().getExtras().get("friendUid").toString();

        setSupportActionBar(this.myToolbar);
        getSupportActionBar().setTitle(friendName);

        this.myPrivateMessagesRef = usersRoot.child(currentUid).child(getString(R.string.Messages)).child(friendUid);
        this.yourPrivateMessagesRef = usersRoot.child(friendUid).child(getString(R.string.Messages)).child(currentUid);

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

        this.myPrivateMessagesRef.addChildEventListener(new ChildEventListener() {
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
            DatabaseReference myMessageKeyRef = myPrivateMessagesRef.push();
            DatabaseReference yourMessageKeyRef = yourPrivateMessagesRef.push();

            Calendar calDate = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yyyy");
            String date = dateFormat.format(calDate.getTime());

            Calendar calTime = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
            String time = timeFormat.format(calTime.getTime());

            HashMap<String, Object> messageToSend = new HashMap<>();

            messageToSend.put("name", this.currentUser.getEmail());
            messageToSend.put("date", date);
            messageToSend.put("time", time);
            messageToSend.put("msg", message);
            myMessageKeyRef.updateChildren(messageToSend).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(MessageViewActivity.this, "Message sent Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            yourMessageKeyRef.updateChildren(messageToSend).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(MessageViewActivity.this, "Message received Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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
            this.privateChatScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}
