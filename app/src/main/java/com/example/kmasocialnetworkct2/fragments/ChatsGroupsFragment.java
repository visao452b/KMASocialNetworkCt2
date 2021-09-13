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

import com.example.kmasocialnetworkct2.activity.CreateChatGroup;
import com.example.kmasocialnetworkct2.adapters.GroupChatAdapter;
import com.example.kmasocialnetworkct2.databinding.FragmentsChatGroupBinding;
import com.example.kmasocialnetworkct2.databinding.FragmentsHomeBinding;
import com.example.kmasocialnetworkct2.models.UserGroups;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ChatsGroupsFragment extends Fragment {
    FragmentsChatGroupBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    public ChatsGroupsFragment() {
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentsChatGroupBinding.inflate(inflater, container, false);
        binding.floatingAddChatGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextActivityAddChatGroup();
            }
        });


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final String uId = auth.getUid();

        final ArrayList<UserGroups> userGroups = new ArrayList<>();
        final GroupChatAdapter groupsAdapter = new GroupChatAdapter(userGroups, getContext());

        binding.chatGroupRecyclerView.setAdapter(groupsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.chatGroupRecyclerView.setLayoutManager(linearLayoutManager);

        database.getReference().child("UserGroups").child(uId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userGroups.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserGroups userGroup = dataSnapshot.getValue(UserGroups.class);
                    userGroups.add(userGroup);
                }
                groupsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    private void nextActivityAddChatGroup() {
        Intent intent = new Intent(getContext(), CreateChatGroup.class);
        startActivity(intent);
    }
}
