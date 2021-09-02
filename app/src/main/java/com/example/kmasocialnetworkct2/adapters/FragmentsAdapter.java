package com.example.kmasocialnetworkct2.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.kmasocialnetworkct2.fragments.MenuFragment;
import com.example.kmasocialnetworkct2.fragments.FriendsFragment;
import com.example.kmasocialnetworkct2.fragments.GroupsFragment;
import com.example.kmasocialnetworkct2.fragments.HomeFragment;
import com.example.kmasocialnetworkct2.fragments.NotificationsFragment;
import com.google.firebase.database.annotations.NotNull;


public class FragmentsAdapter extends FragmentStateAdapter {


    public FragmentsAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new GroupsFragment();
            case 2:
                return new FriendsFragment();
            case 3:
                return new NotificationsFragment();
            case 4:
                return new MenuFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
