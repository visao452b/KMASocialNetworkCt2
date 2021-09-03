package com.example.kmasocialnetworkct2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.kmasocialnetworkct2.adapters.FriendsChatsAdapter;
import com.example.kmasocialnetworkct2.databinding.FragmentsChatsBinding;
import com.example.kmasocialnetworkct2.models.Friends;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class ChatsFragment extends Fragment {
    public ChatsFragment() {
    }

    FragmentsChatsBinding binding;
    ArrayList<Friends> list = new ArrayList<>();

    FirebaseDatabase database;
    FirebaseAuth auth;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentsChatsBinding.inflate(inflater, container, false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final String uId = auth.getUid();
        FriendsChatsAdapter adapter1 = new FriendsChatsAdapter(list, getContext());
        binding.chatRecyclerView.setAdapter(adapter1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Friends").child(uId).orderByChild("msgTimeLast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Friends friends = dataSnapshot.getValue(Friends.class);
                    list.add(friends);
                }

                Collections.reverse(list);

                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



        return binding.getRoot();
    }
}
