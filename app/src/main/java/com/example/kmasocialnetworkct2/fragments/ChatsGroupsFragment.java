package com.example.kmasocialnetworkct2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kmasocialnetworkct2.databinding.FragmentsChatGroupBinding;
import com.example.kmasocialnetworkct2.databinding.FragmentsHomeBinding;

import org.jetbrains.annotations.NotNull;

public class ChatsGroupsFragment extends Fragment {
    FragmentsChatGroupBinding binding;
    public ChatsGroupsFragment() {
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentsChatGroupBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}