package com.example.kmasocialnetworkct2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.adapters.PostAdapter;
import com.example.kmasocialnetworkct2.adapters.PostGroupAdapter;
import com.example.kmasocialnetworkct2.databinding.ActivityGroupBinding;
import com.example.kmasocialnetworkct2.models.Groups;
import com.example.kmasocialnetworkct2.models.Participants;
import com.example.kmasocialnetworkct2.models.Posts;
import com.example.kmasocialnetworkct2.models.UserGroups;
import com.example.kmasocialnetworkct2.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GroupActivity extends AppCompatActivity {
    ActivityGroupBinding binding;
    FirebaseDatabase database;
    ArrayList<Participants> listUser = new ArrayList<>();
    Participants participants;
    Users user;

    ArrayList<Posts> listPost = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String  groupId = getIntent().getStringExtra("groupId");
        String groupName = getIntent().getStringExtra("groupName");

        binding.nameGroupP.setText(groupName);

        database= FirebaseDatabase.getInstance();
        database.getReference().child("GroupsPost").child(groupId).child("participants")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listUser.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            participants = dataSnapshot.getValue(Participants.class);
                            Log.e("UID", participants.getUserId());
                            listUser.add(participants);
                        }
                        binding.sizeMember.setText(listUser.size()+ " Thành Viên");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Users.class);
                        Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.ic_avatar).into(binding.profileImagePost);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        PostGroupAdapter postAdapter = new PostGroupAdapter(getApplicationContext(), listPost, groupId);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        binding.listPostGroup.setLayoutManager(layoutManager1);
        binding.listPostGroup.setAdapter(postAdapter);

        database.getReference().child("PostsGroup").child(groupId).orderByChild("timePost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listPost.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Posts posts = dataSnapshot.getValue(Posts.class);
                    listPost.add(posts);
                }
                Collections.reverse(listPost);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        binding.btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Posts posts = new Posts();
                Date date = new Date();
                posts.setUserPost(user.getUserName());
                posts.setUserIdPost(user.getUserId());
                posts.setTimePost(date.getTime());
                posts.setContentPost(binding.edtPost.getText().toString());
                posts.setFeeling(0);
                String randomKey = database.getReference().push().getKey();
                posts.setPostId(randomKey);

                database.getReference().child("PostsGroup").child(groupId).child(randomKey).setValue(posts).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "On Successful", Toast.LENGTH_SHORT).show();
                        binding.edtPost.setText("");
                    }
                });


            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        database = FirebaseDatabase.getInstance();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        database = FirebaseDatabase.getInstance();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }
}