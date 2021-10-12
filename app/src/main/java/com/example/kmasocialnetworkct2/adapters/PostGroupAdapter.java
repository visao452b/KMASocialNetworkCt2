package com.example.kmasocialnetworkct2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.activity.ProfileActivity;
import com.example.kmasocialnetworkct2.databinding.SamplePostBinding;
import com.example.kmasocialnetworkct2.models.Posts;
import com.example.kmasocialnetworkct2.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostGroupAdapter extends RecyclerView.Adapter<PostGroupAdapter.PostGroupViewHolder> {
    Context contex;
    ArrayList<Posts> listPosts;
    String groupId;

    public PostGroupAdapter(Context contex, ArrayList<Posts> listPosts, String groupId) {
        this.contex = contex;
        this.listPosts = listPosts;
        this.groupId = groupId;
    }

    Users users;


    FirebaseDatabase database;



    @NonNull
    @NotNull
    @Override
    public PostGroupAdapter.PostGroupViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contex).inflate(R.layout.sample_post, parent, false);
        return new PostGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostGroupAdapter.PostGroupViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        Posts posts = listPosts.get(position);
        String id = FirebaseAuth.getInstance().getUid();

        if (posts.getUserIdPost() != null) {
            database.getReference().child("Users").child(posts.getUserIdPost()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    users = snapshot.getValue(Users.class);
                    Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.ic_avatar).into(holder.binding.profileImageShowUserPost);
                    holder.binding.userNamePost.setText(users.getUserName());
                    long time = posts.getTimePost();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                    holder.binding.timePostStatus.setText(dateFormat.format(new Date(time)));
                    holder.binding.contentPost.setText(posts.getContentPost());
                    if (posts.getFeeling()<0){
                        database.getReference().child("Posts").child(posts.getPostId()).child("feeling").setValue(0);
                        holder.binding.sizeTim.setText("0");
                    }else {
                        holder.binding.sizeTim.setText(String.valueOf(posts.getFeeling()));
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        database.getReference().child("PostsGroup").child(groupId).child(posts.getPostId()).child("ListFeeling").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String fl = dataSnapshot.getValue(String.class);
                    if (fl.equals(id)){
                        holder.binding.tim0.setVisibility(View.GONE);
                        holder.binding.tim1.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        holder.binding.profileImageShowUserPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentP = new Intent(contex, ProfileActivity.class);
                intentP.putExtra("uId", posts.getUserIdPost());
                contex.startActivity(intentP);
            }
        });

        holder.binding.tim0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.tim0.setVisibility(View.GONE);
                holder.binding.tim1.setVisibility(View.VISIBLE);
                database.getReference().child("PostsGroup").child(groupId).child(posts.getPostId()).child("ListFeeling").child(id).setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        int count = posts.getFeeling() +1;
                        database.getReference().child("PostsGroup").child(groupId).child(posts.getPostId()).child("feeling").setValue(count);
                    }
                });

            }
        });

        holder.binding.tim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.tim1.setVisibility(View.GONE);
                holder.binding.tim0.setVisibility(View.VISIBLE);
                database.getReference().child("PostsGroup").child(groupId).child(posts.getPostId()).child("ListFeeling").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        int count = posts.getFeeling() -1;
                        database.getReference().child("PostsGroup").child(groupId).child(posts.getPostId()).child("feeling").setValue(count);
                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return listPosts.size();
    }

    public class PostGroupViewHolder extends RecyclerView.ViewHolder {

        SamplePostBinding binding;

        public PostGroupViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            binding = SamplePostBinding.bind(itemView);
        }
    }
}
