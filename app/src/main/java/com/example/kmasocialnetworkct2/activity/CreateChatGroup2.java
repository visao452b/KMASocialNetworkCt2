package com.example.kmasocialnetworkct2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.kmasocialnetworkct2.adapters.FriendsCreateGroupChatAdapter;


import com.example.kmasocialnetworkct2.databinding.ActivityCreateChatGroup2Binding;
import com.example.kmasocialnetworkct2.models.Friends;
import com.example.kmasocialnetworkct2.models.Groups;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateChatGroup2 extends AppCompatActivity {

    ActivityCreateChatGroup2Binding binding;
    ArrayList<Friends> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateChatGroup2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Groups groupIntent = (Groups) getIntent().getExtras().get("group");

        FriendsCreateGroupChatAdapter adapter = new FriendsCreateGroupChatAdapter(list, getApplicationContext(), groupIntent.getGroupName(), groupIntent.getGroupId());
        binding.listFriendGroup.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.listFriendGroup.setLayoutManager(layoutManager);



        binding.titelGroup.setText("Thêm thành viên cho "+groupIntent.getGroupName());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String uId = auth.getUid();

        database.getReference().child("Friends").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Friends friends = dataSnapshot.getValue(Friends.class);
                    list.add(friends);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        binding.floatingAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(CreateChatGroup2.this, ChatsActivity.class);
                startActivity(intent2);
            }
        });
    }
}