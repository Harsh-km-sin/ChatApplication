package com.harshsingh.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
     EditText messageBox;
     ImageView sendButton;
     RecyclerView messageRecyclerView;
     MessageAdapter messageAdapter;
     ArrayList<Message> messageList;
     DatabaseReference mDbRef;
     String senderRoom = null;
     String receiverRoom = null;
    String name, receiverUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        if(intent != null){
             name = intent.getStringExtra("name");
             receiverUid = intent.getStringExtra("uid");
        }

        String senderUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        mDbRef = FirebaseDatabase.getInstance().getReference();

        senderRoom = receiverUid + senderUid;
        receiverRoom = senderUid + receiverUid;


        // Hide ActionBar if available
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(name);
        }


        messageRecyclerView = findViewById(R.id.chatRecyclerView);
        messageBox = findViewById(R.id.messageBox);
        sendButton = findViewById(R.id.send_button);


        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageRecyclerView.setAdapter(messageAdapter);


        mDbRef.child("Chats").child(senderRoom).child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageList.clear();
                for(DataSnapshot userSnapshot : snapshot.getChildren()){
                    Message message = userSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageBox.getText().toString().trim();
                Message messageObject = new Message(message, senderUid);

                mDbRef.child("Chats").child(senderRoom).child("Messages").push()
                        .setValue(messageObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                mDbRef.child("Chats").child(receiverRoom).child("Messages").push()
                                        .setValue(messageObject);
                            }
                        });
                messageBox.setText("");
            }
        });
    }
}