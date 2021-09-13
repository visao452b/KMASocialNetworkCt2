package com.example.kmasocialnetworkct2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.adapters.ChatGroupAdapter;
import com.example.kmasocialnetworkct2.databinding.ActivityChatGroupBinding;
import com.example.kmasocialnetworkct2.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatGroupActivity extends AppCompatActivity {
    ActivityChatGroupBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String  groupId = getIntent().getStringExtra("groupId");
        String groupName = getIntent().getStringExtra("groupName");

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        Date date = new Date();

        binding.groupNameChatGroup.setText(groupName);

        binding.backArrowChatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatGroupActivity.this, ChatsActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();

        final ChatGroupAdapter adapter= new ChatGroupAdapter(messageModels, this);

        binding.chatGroupRecyclarView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.chatGroupRecyclarView.setLayoutManager(linearLayoutManager);

        final String senderId = auth.getUid();
        final String room = groupId;
        database.getReference().child("chats")
                .child(room)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        messageModels.clear();

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            MessageModel model = snapshot1.getValue(MessageModel.class);

                            model.setMessageId(snapshot1.getKey());

                            messageModels.add(model);
                        }

                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendChatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.edtMessageChatGroup.getText().toString();
                final MessageModel model = new MessageModel(message, auth.getUid(), date.getTime());
                model.setTimestamp(new Date().getTime());
                binding.edtMessageChatGroup.setText("");

                database.getReference().child("chats")
                        .child(room)
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });


    }
}