package com.example.kmasocialnetworkct2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.adapters.PostAdapter;
import com.example.kmasocialnetworkct2.databinding.ActivityProfileBinding;
import com.example.kmasocialnetworkct2.models.Friends;
import com.example.kmasocialnetworkct2.models.Posts;
import com.example.kmasocialnetworkct2.models.ProfileUser;
import com.example.kmasocialnetworkct2.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    Users user;
    Friends f1, f2;
    ArrayList<Posts> listPost = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         binding = ActivityProfileBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intentP = getIntent();
        String uIdP = intentP.getStringExtra("uId");

        if (uIdP.equals(auth.getUid())){
            binding.addFriend.setVisibility(View.GONE);
            binding.message.setVisibility(View.GONE);
        }

        database.getReference().child("Users").child(uIdP).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                user = snapshot.getValue(Users.class);
                Picasso.get().load(user.getProfilepic()).placeholder(R.drawable.ic_avatar).into(binding.avatarProfile);
                binding.nameProfile.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        database.getReference().child("Profiles").child(uIdP).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ProfileUser profileUser = snapshot.getValue(ProfileUser.class);

                try {
                    binding.edtWork.setText(profileUser.getWork());
                    binding.edtLive.setText(profileUser.getLive());
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        binding.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChatDetailActivity.class);
                intent.putExtra("userId", user.getUserId());
                intent.putExtra("profilePic", user.getProfilepic());
                intent.putExtra("userName", user.getUserName());
                startActivity(intent);
            }
        });

        database.getReference().child("Users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                f1 = snapshot.getValue(Friends.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        database.getReference().child("Users").child(uIdP).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                f2 = snapshot.getValue(Friends.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        PostAdapter postAdapter = new PostAdapter(getApplicationContext(), listPost);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.listPostProfile.setLayoutManager(layoutManager);
        binding.listPostProfile.setAdapter(postAdapter);


        database.getReference().child("Posts").orderByChild("timePost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                listPost.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Posts posts = dataSnapshot.getValue(Posts.class);
                    if (posts.getUserIdPost().equals(uIdP)) {
                        listPost.add(posts);
                    }
                }
                Collections.reverse(listPost);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}