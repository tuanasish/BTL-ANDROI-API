package com.example.btl;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.btl.fragments.AccountFragment;
import com.example.btl.fragments.BookedFieldsFragment;
import com.example.btl.fragments.ListFieldsFragment;
import com.example.btl.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private User loginUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Gắn Toolbar với ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Hiển thị fragment mặc định khi mở app
        loadFragment(new ListFieldsFragment());

        loginUser = (User) getIntent().getSerializableExtra("USER_DATA");
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.nav_list_fields) {
                selectedFragment = new ListFieldsFragment();
            } else if (id == R.id.nav_booked_fields) {
                selectedFragment = new BookedFieldsFragment();
            } else if (id == R.id.nav_account) {
                AccountFragment accountFragment = new AccountFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER_DATA", loginUser);
                Log.d("DEBUG", "User: " + loginUser);
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
                    .replace(R.id.frame_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
