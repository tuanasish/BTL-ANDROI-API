package com.example.btl;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.btl.fragments.AccountFragment;
import com.example.btl.fragments.BookedFieldsFragment;
import com.example.btl.fragments.DiscountFragment;
import com.example.btl.fragments.ListFieldsFragment;
import com.example.btl.fragments.MapFragment;
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
        getSupportActionBar().setTitle("Booking App");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more_vert_white));

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Hiển thị fragment mặc định khi mở app
        loadFragment(new ListFieldsFragment());

        loginUser = (User) getIntent().getSerializableExtra("USER_DATA");

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int id = item.getItemId();
            if (id == R.id.nav_list_fields) {
                selectedFragment = attachUserToFragment(new ListFieldsFragment());

            } else if (id == R.id.nav_account) {
                AccountFragment accountFragment = new AccountFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER_DATA", loginUser);
                Log.d("DEBUG", "User: " + loginUser);
                accountFragment.setArguments(bundle);
                selectedFragment = accountFragment;
            } else if (id == R.id.nav_discount) {
                selectedFragment = new DiscountFragment();
            } else if (id == R.id.nav_map) {
                // Sử dụng MapFragment để hiển thị bản đồ
                selectedFragment = new MapFragment();
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
    private Fragment attachUserToFragment(Fragment fragment) {
        if (loginUser != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("USER_DATA", loginUser);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

}
