package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.btl.R;
import com.example.btl.fragments.ManageFieldsFragment;
import com.example.btl.fragments.UserManagementFragment; // Đảm bảo rằng fragment này được import
/*
import com.example.btl.fragments.AdminAccountFragment; // Đảm bảo rằng fragment này được import
*/

public class AdminMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        // Ánh xạ BottomNavigationView từ layout
        bottomNavigationView = findViewById(R.id.bottom_navigation_admin);

        // Load fragment mặc định khi mở ứng dụng (Quản lý sân)
        loadFragment(new ManageFieldsFragment());

        // Xử lý sự kiện khi người dùng chọn item trong BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // Sử dụng if-else để chọn fragment tương ứng
            if (item.getItemId() == R.id.nav_manage_fields) {
                selectedFragment = new ManageFieldsFragment(); // Fragment quản lý sân
            } else if (item.getItemId() == R.id.nav_manage_users) {
                selectedFragment = new UserManagementFragment(); // Fragment quản lý người dùng
            } /* else if (item.getItemId() == R.id.nav_admin_account) {
        selectedFragment = new AdminAccountFragment(); // Fragment tài khoản admin
    } */

            return loadFragment(selectedFragment); // Load fragment đã chọn
        });

    }

    // Phương thức để load fragment vào FrameLayout
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container_admin, fragment) // Thay đổi nội dung trong FrameLayout
                    .commit();
            return true;
        }
        return false;
    }
}
