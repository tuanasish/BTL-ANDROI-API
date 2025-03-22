package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.btl.R;
import com.example.btl.adapters.DetailFragentAdapterField;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FieldDetailActivity extends AppCompatActivity {

    private ImageView fieldImage;
    private TextView fieldName, fieldAddress, fieldNumber;
    private Button btnBookNow;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private DetailFragentAdapterField adapterDetailField;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);

        fieldImage = findViewById(R.id.fieldImage);
        fieldName = findViewById(R.id.fieldName);
        fieldAddress = findViewById(R.id.fieldAddress);
        fieldNumber = findViewById(R.id.fieldNumber);
        btnBookNow = findViewById(R.id.btnBookNow);
        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Tạo adapter cho ViewPager2
        adapterDetailField = new DetailFragentAdapterField(this);
        viewPager2.setAdapter(adapterDetailField);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Thông tin");
                            break;
                        case 1:
                            tab.setText("Dịch vụ");
                            break;
                        case 2:
                            tab.setText("Hình ảnh");
                            break;
                        case 3:
                            tab.setText("Đánh giá");
                            break;
                    }
                }).attach();

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String address = intent.getStringExtra("address");
            String number = intent.getStringExtra("number");
            int image = intent.getIntExtra("image", R.drawable.ic_launcher_background);

            fieldName.setText(name);
            fieldAddress.setText(address);
            fieldNumber.setText("Số điện thoại: " + number);
            fieldImage.setImageResource(image);

            // Xử lý sự kiện khi nhấn nút "Đặt ngay"
            btnBookNow.setOnClickListener(v -> {
                Intent bookingIntent = new Intent(FieldDetailActivity.this, BookingActivity.class);
                bookingIntent.putExtra("name", name);
                bookingIntent.putExtra("address", address);
                bookingIntent.putExtra("number", number);
                bookingIntent.putExtra("image", image);
                startActivity(bookingIntent);
            });
        }
    }
}
