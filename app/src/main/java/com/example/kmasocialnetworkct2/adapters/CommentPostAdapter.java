package com.example.kmasocialnetworkct2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.databinding.SampleCommentsPostBinding;
import com.example.kmasocialnetworkct2.models.Comments;
import com.example.kmasocialnetworkct2.models.Posts;
import com.example.kmasocialnetworkct2.models.Users;
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

public class CommentPostAdapter extends RecyclerView.Adapter<CommentPostAdapter.CommentPostViewHolder>{

    Context contex;
    ArrayList<Comments> listComment;

    public CommentPostAdapter(Context contex, ArrayList<Comments> listComment) {
        this.contex = contex;
        this.listComment = listComment;
    }

    Users users;
    FirebaseDatabase database;

    @NonNull
    @NotNull
    @Override
    public CommentPostViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contex).inflate(R.layout.sample_comments_post, parent, false);
        return new CommentPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentPostAdapter.CommentPostViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance();
        Comments comment = listComment.get(position);
        String id = FirebaseAuth.getInstance().getUid();

        if (comment.getUserIdComment() != null) {
            database.getReference().child("Users").child(comment.getUserIdComment()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    users = snapshot.getValue(Users.class);
                    Picasso.get().load(users.getProfilepic()).placeholder(R.drawable.ic_avatar).into(holder.binding.profileImageShowUserCMPost);
                    holder.binding.userNameCMPost.setText(users.getUserName());
                    long time = comment.getTimeCommentPost();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
                    holder.binding.timeCMPostStatus.setText(dateFormat.format(new Date(time)));
                    holder.binding.contentComment.setText(comment.getContentComment());
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    public class CommentPostViewHolder extends RecyclerView.ViewHolder {
        SampleCommentsPostBinding binding;
        public CommentPostViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding = SampleCommentsPostBinding.bind(itemView);
        }
    }
}
