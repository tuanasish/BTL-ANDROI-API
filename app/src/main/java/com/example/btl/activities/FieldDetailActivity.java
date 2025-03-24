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
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiFieldService;
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
    private ApiFieldService apiFieldService;
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

        // Nhận ID từ Intent
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("FIELD_DATA")) {
            field = (Field) intent.getSerializableExtra("FIELD_DATA");
            // Sử dụng dữ liệu field theo ý muốn
        }

        fieldAddress.setText(field.getLocation());
        fieldName.setText(field.getName());
        fieldImage.setImageResource(R.drawable.field2);



            // Xử lý sự kiện khi nhấn nút "Đặt ngay"
            btnBookNow.setOnClickListener(v -> {
                Intent bookingIntent = new Intent(FieldDetailActivity.this, BookingActivity.class);
                /*bookingIntent.putExtra("name", name);
                bookingIntent.putExtra("address", address);
                bookingIntent.putExtra("number", number);
                bookingIntent.putExtra("image", image);*/
                bookingIntent.putExtra("FIELD_DATA", field);
                startActivity(bookingIntent);
            });
        }

//    private void loadFieldDetails(int id) {
//        apiFieldService.getFieldById(id, new ApiFieldService.ApiCallback<Field>() {
//            @Override
//            public void onSuccess(Field field) {
//                // Cập nhật giao diện với dữ liệu nhận được
//                fieldName.setText(field.getName());
//                fieldAddress.setText(field.getLocation());
//                fieldNumber.setText(String.valueOf(field.getCapacity()));
//
//                if (field.getImages() != null && !field.getImages().isEmpty()) {
//                    Glide.with(FieldDetailActivity.this).load(field.getImages()).into(fieldImage);
//                } else {
//                    fieldImage.setImageResource(R.drawable.field2);
//                }
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.e("API_ERROR", "Lỗi khi tải dữ liệu sân bóng: " + t.getMessage());
//                Toast.makeText(FieldDetailActivity.this, "Lỗi khi tải dữ liệu sân bóng", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}

