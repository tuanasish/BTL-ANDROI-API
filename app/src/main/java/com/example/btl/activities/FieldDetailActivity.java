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
import com.example.btl.BookingActivity;
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

        fieldImage = findViewById(R.id.fieldImage);
        fieldName = findViewById(R.id.fieldName);
        fieldAddress = findViewById(R.id.fieldAddress);
        fieldNumber = findViewById(R.id.fieldNumber);
        btnBookNow = findViewById(R.id.btnBookNow);
        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        adapterDetailField = new DetailFragentAdapterField(this);
        viewPager2.setAdapter(adapterDetailField);

        Intent intent = getIntent();
        fieldId = intent.getIntExtra("FIELD_ID", -1);
        if (fieldId != -1) {
            loadFieldById(fieldId);
        } else {
            Toast.makeText(this, "Không tìm thấy ID sân!", Toast.LENGTH_SHORT).show();
            finish();
        }

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

        btnBookNow.setOnClickListener(v -> {
            if (field != null) {
                Intent bookingIntent = new Intent(FieldDetailActivity.this, BookingActivity.class);
                bookingIntent.putExtra("FIELD_ID", field.getField_id());
                startActivity(bookingIntent);
            }
        });
    }

    private void loadFieldById(int id) {
        ApiFieldInterface apiFieldInterface = ApiClient.getClient().create(ApiFieldInterface.class);
        apiFieldService = new ApiFieldService(apiFieldInterface);

        apiFieldService.getFieldById(id, new ApiFieldService.ApiCallback<Field>() {
            @Override
            public void onSuccess(Field fieldResponse) {
                field = fieldResponse;

                fieldName.setText(field.getName());
                fieldAddress.setText(field.getLocation());
                fieldNumber.setText(String.valueOf(field.getCapacity()));

                if (field.getImages() != null && !field.getImages().isEmpty()) {
                    Glide.with(FieldDetailActivity.this)
                            .load(field.getImages())
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(fieldImage);
                } else {
                    fieldImage.setImageResource(R.drawable.field2);
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(FieldDetailActivity.this, "Lỗi khi tải dữ liệu sân", Toast.LENGTH_SHORT).show();
                Log.e("FieldDetailActivity", "Lỗi API: " + t.getMessage());
            }
        });
    }
}
