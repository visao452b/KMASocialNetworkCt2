package com.example.kmasocialnetworkct2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.kmasocialnetworkct2.adapters.CommentPostAdapter;
import com.example.kmasocialnetworkct2.databinding.ActivityCommentsBinding;
import com.example.kmasocialnetworkct2.models.Comments;
import com.example.kmasocialnetworkct2.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class CommentsActivity extends AppCompatActivity {
    ActivityCommentsBinding binding;
    ArrayList<Comments> list = new ArrayList<>();
    FirebaseDatabase database;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String  postID = getIntent().getStringExtra("postID");
        database = FirebaseDatabase.getInstance();


        CommentPostAdapter commentPostAdapter = new CommentPostAdapter(getApplicationContext(), list);
        binding.listComments.setAdapter(commentPostAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.listComments.setLayoutManager(layoutManager);

        if (postID!=null){
            database.getReference().child("Posts").child(postID).child("comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Comments comments = dataSnapshot.getValue(Comments.class);
                        list.add(comments);
                    }
                    commentPostAdapter.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError error) {
                }
            });
        }

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(Users.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                String randomKey = database.getReference().push().getKey();
                Comments comments = new Comments(user.getUserName(),user.getUserId(), binding.edtMessage.getText().toString(), randomKey,date.getTime());
                database.getReference().child("Posts").child(postID).child("comments").child(randomKey).setValue(comments).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        binding.edtMessage.setText("");
                        Log.i("Comment success: ",comments.getContentComment());
                    }
                });
            }
        });


    }
}