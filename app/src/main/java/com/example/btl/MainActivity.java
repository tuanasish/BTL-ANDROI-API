package com.example.btl;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // ✅ CHỈNH Ở ĐÂY
import androidx.fragment.app.Fragment;

import com.example.btl.fragments.AccountFragment;
import com.example.btl.fragments.BookedFieldsFragment;
import com.example.btl.fragments.ListFieldsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gắn Toolbar với ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // ✅ Đúng cách

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Hiển thị fragment mặc định khi mở app
        loadFragment(new ListFieldsFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.nav_list_fields) {
                selectedFragment = new ListFieldsFragment();
            } else if (id == R.id.nav_booked_fields) {
                selectedFragment = new BookedFieldsFragment();
            } else if (id == R.id.nav_account) {
                selectedFragment = new AccountFragment();
            }

            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
