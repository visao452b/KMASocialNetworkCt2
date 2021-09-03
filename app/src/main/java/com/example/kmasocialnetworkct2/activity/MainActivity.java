package com.example.kmasocialnetworkct2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.kmasocialnetworkct2.R;
import com.example.kmasocialnetworkct2.adapters.FragmentsAdapter;
import com.example.kmasocialnetworkct2.animation.DepthPageTransformer;
import com.example.kmasocialnetworkct2.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;


import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbarMain);



        FragmentsAdapter fragmentsAdapter = new FragmentsAdapter(this);
        binding.viewPagerMain.setAdapter(fragmentsAdapter);
        binding.viewPagerMain.setPageTransformer(new DepthPageTransformer());

        binding.bottomNavigationMain.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                int id = item.getItemId();
                selectItem(id);
                return true;
            }
        });

        binding.viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                callBack(position);
            }
        });
    }


    private void selectItem(int id) {
        switch (id) {
            case R.id.bottom_home:
                binding.viewPagerMain.setCurrentItem(0);
                break;
            case R.id.bottom_group:
                binding.viewPagerMain.setCurrentItem(1);
                break;
            case R.id.bottom_friend:
                binding.viewPagerMain.setCurrentItem(2);
                break;
            case R.id.bottom_notification:
                binding.viewPagerMain.setCurrentItem(3);
                break;
            case R.id.bottom_menu:
                binding.viewPagerMain.setCurrentItem(4);
                break;
        }
    }

    private void callBack(int position) {
        switch (position) {
            case 0:
                binding.bottomNavigationMain.getMenu().findItem(R.id.bottom_home).setChecked(true);
                break;
            case 1:
                binding.bottomNavigationMain.getMenu().findItem(R.id.bottom_group).setChecked(true);
                break;
            case 2:
                binding.bottomNavigationMain.getMenu().findItem(R.id.bottom_friend).setChecked(true);
                break;
            case 3:
                binding.bottomNavigationMain.getMenu().findItem(R.id.bottom_notification).setChecked(true);
                break;
            case 4:
                binding.bottomNavigationMain.getMenu().findItem(R.id.bottom_menu).setChecked(true);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btnSearch) {
            Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show(); 
        }else if (item.getItemId() == R.id.btnLogout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.btnMessage) {
            Intent intent  = new Intent(MainActivity.this, ChatsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}