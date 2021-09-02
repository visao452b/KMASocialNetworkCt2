package com.example.kmasocialnetworkct2.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kmasocialnetworkct2.activity.FindFriends;
import com.example.kmasocialnetworkct2.databinding.FragmentsFriendsBinding;


public class FriendsFragment extends Fragment {

    public FriendsFragment() {
    }

    FragmentsFriendsBinding binding;
//    ArrayList<Friends> list = new ArrayList<>();
//    FirebaseDatabase database;
//    FirebaseAuth auth;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentsFriendsBinding.inflate(inflater, container, false);

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
