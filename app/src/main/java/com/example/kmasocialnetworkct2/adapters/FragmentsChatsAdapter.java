package com.example.kmasocialnetworkct2.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kmasocialnetworkct2.fragments.ChatsFragment;

import com.example.kmasocialnetworkct2.fragments.ChatsGroupsFragment;
import com.example.kmasocialnetworkct2.fragments.GroupsFragment;

import com.example.kmasocialnetworkct2.fragments.WaitingChatsFragment;

import org.jetbrains.annotations.NotNull;

public class FragmentsChatsAdapter extends FragmentStateAdapter {

    public FragmentsChatsAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChatsFragment();
            case 1:
                return new ChatsGroupsFragment();
            case 2:
                return new WaitingChatsFragment();
            default:
                return new ChatsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
