package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.btl.R;
import com.example.btl.adapters.DetailFragentAdapterField;
import com.example.btl.api.ApiFieldService;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiClient;
import com.example.btl.models.Field;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FieldDetailActivity extends AppCompatActivity {

    private int fieldId;
    private ImageView fieldImage;
    private TextView fieldName, fieldAddress, fieldNumber;
    private Button btnBookNow;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private DetailFragentAdapterField adapterDetailField;
    private Field field;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_detail);

        // Ánh xạ các thành phần giao diện
        fieldImage = findViewById(R.id.fieldImage);
        fieldName = findViewById(R.id.fieldName);
        fieldAddress = findViewById(R.id.fieldAddress);
        fieldNumber = findViewById(R.id.fieldNumber);
        btnBookNow = findViewById(R.id.btnBookNow);
        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Tạo adapter cho ViewPager2 để hiển thị thông tin chi tiết sân
        adapterDetailField = new DetailFragentAdapterField(this);
        viewPager2.setAdapter(adapterDetailField);

        // Nhận fieldId từ Intent
        fieldId = getIntent().getIntExtra("FIELD_ID", -1);

        if (fieldId != -1) {
            // Gọi API để lấy thông tin sân bằng fieldId
            ApiFieldInterface apiInterface = ApiClient.getClient().create(ApiFieldInterface.class);
            ApiFieldService apiFieldService = new ApiFieldService(apiInterface);

            apiFieldService.getFieldById(fieldId, new ApiFieldService.ApiCallback<Field>() {
                @Override
                public void onSuccess(Field result) {
                    field = result;

                    // Hiển thị thông tin sân
                    fieldName.setText(field.getName());
                    fieldAddress.setText("Địa chỉ: " + field.getLocation());
                    fieldNumber.setText("Sức chứa: " + field.getCapacity());

                    // Hiển thị ảnh bằng Glide
                    if (field.getImages() != null && !field.getImages().isEmpty()) {
                        Glide.with(FieldDetailActivity.this)
                                .load(field.getImages())
                                .placeholder(R.drawable.ic_launcher_background)
                                .into(fieldImage);
                    } else {
                        fieldImage.setImageResource(R.drawable.field2);
                    }

                    // Cập nhật thông tin TabLayout
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
                }

                @Override
                public void onError(Throwable t) {
                    // Xử lý lỗi khi không lấy được dữ liệu
                    Toast.makeText(FieldDetailActivity.this, "Lỗi tải thông tin sân!", Toast.LENGTH_SHORT).show();
                    Log.e("FieldDetailActivity", "Lỗi khi gọi API: ", t);
                }
            });
        }

        // Xử lý sự kiện khi nhấn nút "Đặt ngay"
        btnBookNow.setOnClickListener(v -> {
            if (field != null) {
                // Lấy thông tin từ đối tượng Field
                String fieldName = field.getName();
                String fieldAddress = field.getLocation();
                int fieldPhone = field.getCapacity();  // Hoặc số điện thoại của bạn
                String fieldImage = field.getImages();
                int fieldId = field.getField_id();

                // Log dữ liệu trước khi truyền vào Intent
                Log.d("FieldDetailActivity", "fieldName: " + fieldName);
                Log.d("FieldDetailActivity", "fieldAddress: " + fieldAddress);
                Log.d("FieldDetailActivity", "fieldPhone: " + fieldPhone);
                Log.d("FieldDetailActivity", "fieldImage: " + fieldImage);
                Log.d("FieldDetailActivity", "fieldId: " + fieldId);

                // Tạo một Intent để chuyển tới BookingActivity
                Intent bookingIntent = new Intent(FieldDetailActivity.this, BookingActivity.class);

                // Truyền các thông tin của Field qua Intent
                bookingIntent.putExtra("FIELD_NAME", fieldName);
                bookingIntent.putExtra("FIELD_ADDRESS", fieldAddress);
                bookingIntent.putExtra("FIELD_PHONE", String.valueOf(fieldPhone));  // Chuyển phone thành String nếu cần
                bookingIntent.putExtra("FIELD_IMAGE", fieldImage);
                bookingIntent.putExtra("FIELD_ID", fieldId);

                // Bắt đầu Activity
                startActivity(bookingIntent);
            }
        });
    }
}
