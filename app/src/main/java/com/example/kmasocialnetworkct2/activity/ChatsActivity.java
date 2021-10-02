package com.example.kmasocialnetworkct2.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.adapters.FragmentsChatsAdapter;
import com.example.kmasocialnetworkct2.animation.DepthPageTransformer;
import com.example.kmasocialnetworkct2.databinding.ActivityChatsBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class ChatsActivity extends AppCompatActivity {
    ActivityChatsBinding binding;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentsChatsAdapter fragmentsChatsAdapter = new FragmentsChatsAdapter(this);
        binding.viewPagerChat.setAdapter(fragmentsChatsAdapter);
        binding.viewPagerChat.setPageTransformer(new DepthPageTransformer());

        binding.bottomNavigationChat.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                selectItem(id);
                return true;
            }
        });

        binding.viewPagerChat.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                callBack(position);
            }
        });
    }

    private void callBack(int position) {
        switch (position) {
            case 0:
                binding.bottomNavigationChat.getMenu().findItem(R.id.bottom_chat).setChecked(true);
                break;
            case 1:
                binding.bottomNavigationChat.getMenu().findItem(R.id.bottom_chatGroup).setChecked(true);
                break;
            case 2:
                binding.bottomNavigationChat.getMenu().findItem(R.id.bottom_waiting_chat).setChecked(true);
                break;
        }
    }

    private void selectItem(int id) {
        switch (id) {
            case R.id.bottom_chat:
                binding.viewPagerChat.setCurrentItem(0);
                break;
            case R.id.bottom_chatGroup:
                binding.viewPagerChat.setCurrentItem(1);
                break;
            case R.id.bottom_waiting_chat:
                binding.viewPagerChat.setCurrentItem(2);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database = FirebaseDatabase.getInstance();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        database = FirebaseDatabase.getInstance();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }
}