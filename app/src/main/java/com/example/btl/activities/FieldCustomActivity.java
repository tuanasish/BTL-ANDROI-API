package com.example.btl.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.btl.R;

public class FieldCustomActivity extends AppCompatActivity {

    private TextView fieldName, fieldLocation, fieldCapacity, fieldDescription;
    private ImageView fieldImage;
    private Button btnEditField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_custom); // Đảm bảo layout tồn tại

        // Ánh xạ các view
        fieldName = findViewById(R.id.fieldName);
        fieldLocation = findViewById(R.id.fieldLocation);
        fieldCapacity = findViewById(R.id.fieldCapacity);
        fieldDescription = findViewById(R.id.fieldDescription);
        fieldImage = findViewById(R.id.fieldImage);
        btnEditField = findViewById(R.id.btnEditField);

        // Nhận thông tin từ Intent
        fieldName.setText(getIntent().getStringExtra("FIELD_NAME"));
        fieldLocation.setText(getIntent().getStringExtra("FIELD_LOCATION"));
        fieldCapacity.setText("Sức chứa: " + getIntent().getIntExtra("FIELD_CAPACITY", 0));
        fieldDescription.setText(getIntent().getStringExtra("FIELD_DESCRIPTION"));

        // Load hình ảnh từ URL nếu có
        String imageUrl = getIntent().getStringExtra("FIELD_IMAGE");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(fieldImage);
        } else {
            fieldImage.setImageResource(R.drawable.ic_launcher_background); // Placeholder
        }

        // Sự kiện chỉnh sửa sân
        btnEditField.setOnClickListener(v -> {
            // Mở màn hình chỉnh sửa sân hoặc xử lý logic chỉnh sửa
        });
    }
}
