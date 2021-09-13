package com.example.kmasocialnetworkct2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kmasocialnetworkct2.activity.FindFriends;
import com.example.kmasocialnetworkct2.adapters.FriendsAdapter;
import com.example.kmasocialnetworkct2.databinding.FragmentsFriendsBinding;
import com.example.kmasocialnetworkct2.models.Friends;
import com.example.kmasocialnetworkct2.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {

    public FriendsFragment() {
    }

    FragmentsFriendsBinding binding;
    ArrayList<Friends> list = new ArrayList<>();
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentsFriendsBinding.inflate(inflater, container, false);

        FriendsAdapter adapter = new FriendsAdapter(list, getContext());
        binding.friendsRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.friendsRecyclerView.setLayoutManager(layoutManager);

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
                nextActivityFindFriend();
            }
        });

        return binding.getRoot();
    }

    private void nextActivityFindFriend() {
        Intent intent = new Intent(getContext(), FindFriends.class);
        startActivity(intent);
    }

}
