package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.btl.R;
import com.example.btl.fragments.AdminAccountFragment;
import com.example.btl.fragments.ManageFieldsFragment;
import com.example.btl.fragments.UserManagementFragment;
import com.example.btl.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    User loginUser;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more_vert_white));
        // Lấy user từ LoginActivity
        Intent intent = getIntent();
        loginUser = (User) intent.getSerializableExtra("USER_DATA");

        loadFragment(new ManageFieldsFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_manage_fields) {
                selectedFragment = new ManageFieldsFragment();
            } else if (item.getItemId() == R.id.nav_manage_users) {
                selectedFragment = new UserManagementFragment();
            } else if (item.getItemId() == R.id.nav_admin_account) {
                AdminAccountFragment accountFragment = new AdminAccountFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER_DATA", loginUser); // Truyền user
                accountFragment.setArguments(bundle);
                selectedFragment = accountFragment;
            }

            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container_admin, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
