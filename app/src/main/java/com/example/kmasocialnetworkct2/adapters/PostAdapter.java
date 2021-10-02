package com.example.kmasocialnetworkct2.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.databinding.SamplePostBinding;
import com.example.kmasocialnetworkct2.models.ListFeeling;
import com.example.kmasocialnetworkct2.models.Participants;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    Context contex;
    ArrayList<Posts> listPosts;
//    ArrayList<ListFeeling> listFeelings;
    ListFeeling listFeeling;

    Users users;
//    String like;


    FirebaseDatabase database;

    public PostAdapter(Context contex, ArrayList<Posts> listPosts) {
        this.contex = contex;
        this.listPosts = listPosts;
    }

    @NonNull
    @NotNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contex).inflate(R.layout.sample_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostAdapter.PostViewHolder holder, int position) {
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
                    holder.binding.sizeTim.setText(String.valueOf(posts.getFeeling()));
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        database.getReference().child("Posts").child(posts.getPostId()).child("ListFeeling").addValueEventListener(new ValueEventListener() {
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



        holder.binding.tim0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.tim0.setVisibility(View.GONE);
                holder.binding.tim1.setVisibility(View.VISIBLE);
                database.getReference().child("Posts").child(posts.getPostId()).child("ListFeeling").child(id).setValue(id).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        int count = posts.getFeeling() +1;
                        database.getReference().child("Posts").child(posts.getPostId()).child("feeling").setValue(count);
                    }
                });

            }
        });

        holder.binding.tim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.tim1.setVisibility(View.GONE);
                holder.binding.tim0.setVisibility(View.VISIBLE);
                database.getReference().child("Posts").child(posts.getPostId()).child("ListFeeling").child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        int count = posts.getFeeling() -1;
                        database.getReference().child("Posts").child(posts.getPostId()).child("feeling").setValue(count);
                    }
                });

            }
        });

//        database.getReference().child("Posts").child(posts.getPostId()).child("feeling").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                like = snapshot.getValue(String.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });


//        holder.binding.feelingPost.setText(posts.getFeeling());




//        database.getReference().child("Posts").child(posts.getPostId()).child("feeling").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                like = snapshot.getValue(Integer.class);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });
//        int like = posts.getFeeling();
//            holder.binding.feelingPost.setText(like);

//        holder.binding.btnLike.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int like = posts.getFeeling() +1;
//                database.getReference().child("Posts").child(posts.getPostId()).child("feeling").setValue(like).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        holder.binding.feelingPost.setText(like);
//                    }
//                });
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return listPosts.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        SamplePostBinding binding;

        public PostViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            binding = SamplePostBinding.bind(itemView);
        }
    }
}