package com.example.kmasocialnetworkct2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.adapters.FriendsCreateGroupAdapter;
import com.example.kmasocialnetworkct2.adapters.FriendsCreateGroupChatAdapter;
import com.example.kmasocialnetworkct2.databinding.ActivityCreateGroup2Binding;
import com.example.kmasocialnetworkct2.models.Friends;
import com.example.kmasocialnetworkct2.models.Groups;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateGroup2 extends AppCompatActivity {
    ActivityCreateGroup2Binding binding;
    ArrayList<Friends> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateGroup2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Groups groupIntent = (Groups) getIntent().getExtras().get("group");

        FriendsCreateGroupAdapter adapter = new FriendsCreateGroupAdapter(list, getApplicationContext(), groupIntent.getGroupName(), groupIntent.getGroupId());
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
                Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
                intent.putExtra("groupId", groupIntent.getGroupId());
                intent.putExtra("groupName", groupIntent.getGroupName());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }
}