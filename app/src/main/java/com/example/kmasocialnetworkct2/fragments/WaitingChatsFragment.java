package com.example.kmasocialnetworkct2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kmasocialnetworkct2.databinding.FragmentsChatGroupBinding;
import com.example.kmasocialnetworkct2.databinding.FragmentsWaitingChatsBinding;

import org.jetbrains.annotations.NotNull;

public class WaitingChatsFragment extends Fragment {
    public WaitingChatsFragment() {
    }

    FragmentsWaitingChatsBinding binding;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = FragmentsWaitingChatsBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
