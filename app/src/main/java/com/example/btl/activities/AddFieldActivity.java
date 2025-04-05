package com.example.btl.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.fragments.ManageFieldsFragment;
import com.example.btl.models.Field;

public class AddFieldActivity extends AppCompatActivity {

    private EditText fieldName, fieldLocation, fieldPrice, fieldCapacity, fieldDescription, fieldCoordinates;
    private ImageView fieldImage;
    private Button btnAddField, btnSelectImage;
    private static final int PICK_IMAGE_REQUEST = 1; // Request code cho chọn ảnh
    private Uri imageUri;  // Lưu trữ URI của ảnh đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_field);

        // Ánh xạ các phần tử
        fieldName = findViewById(R.id.fieldName);
        fieldLocation = findViewById(R.id.fieldLocation);
        fieldPrice = findViewById(R.id.fieldPrice);
        fieldCapacity = findViewById(R.id.fieldCapacity);
        fieldDescription = findViewById(R.id.fieldDescription);
        fieldCoordinates = findViewById(R.id.fieldCoordinates);
        fieldImage = findViewById(R.id.fieldImage);
        btnAddField = findViewById(R.id.btnAddField);
        btnSelectImage = findViewById(R.id.btnSelectImage);

        // Sự kiện nhấn nút "Chọn ảnh"
        btnSelectImage.setOnClickListener(v -> openFileChooser());

        // Sự kiện nhấn nút "Thêm sân"
        btnAddField.setOnClickListener(v -> {
            // Validate input fields
            if (validateInputFields()) {
                // Lấy giá trị từ các trường input
                String name = fieldName.getText().toString();
                String location = fieldLocation.getText().toString();
                double price = Double.parseDouble(fieldPrice.getText().toString());
                int capacity = Integer.parseInt(fieldCapacity.getText().toString());
                String description = fieldDescription.getText().toString();
                String coordinates = fieldCoordinates.getText().toString();
                String imagePath = imageUri != null ? imageUri.toString() : "default_image.png";

                // Tạo đối tượng Field mới
                int fieldId = generateFieldId();
                Field newField = new Field(capacity, description, fieldId, imagePath, location, name, price, "Football");

                // Add the field to the static list in ManageFieldsFragment
                ManageFieldsFragment.addField(newField);

                // Thông báo cho người dùng
                Toast.makeText(AddFieldActivity.this, "Sân đã được thêm thành công", Toast.LENGTH_SHORT).show();

                // Quay lại màn hình quản lý sân
                finish();
            }
        });
    }

    // Validate user input
    private boolean validateInputFields() {
        boolean isValid = true;

        // Check if name is empty
        if (fieldName.getText().toString().trim().isEmpty()) {
            fieldName.setError("Vui lòng nhập tên sân");
            isValid = false;
        }

        // Check if location is empty
        if (fieldLocation.getText().toString().trim().isEmpty()) {
            fieldLocation.setError("Vui lòng nhập địa chỉ");
            isValid = false;
        }

        // Check if price field is valid
        try {
            String priceStr = fieldPrice.getText().toString().trim();
            if (priceStr.isEmpty()) {
                fieldPrice.setError("Vui lòng nhập giá");
                isValid = false;
            } else {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    fieldPrice.setError("Giá phải lớn hơn 0");
                    isValid = false;
                }
            }
        } catch (NumberFormatException e) {
            fieldPrice.setError("Vui lòng nhập giá hợp lệ");
            isValid = false;
        }

        // Check if capacity is valid
        try {
            String capacityStr = fieldCapacity.getText().toString().trim();
            if (capacityStr.isEmpty()) {
                fieldCapacity.setError("Vui lòng nhập sức chứa");
                isValid = false;
            } else {
                int capacity = Integer.parseInt(capacityStr);
                if (capacity <= 0) {
                    fieldCapacity.setError("Sức chứa phải lớn hơn 0");
                    isValid = false;
                }
            }
        } catch (NumberFormatException e) {
            fieldCapacity.setError("Vui lòng nhập sức chứa hợp lệ");
            isValid = false;
        }

        return isValid;
    }

    // Mở thư viện ảnh để người dùng chọn ảnh
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");  // Chỉ cho phép chọn ảnh
        startActivityForResult(intent, PICK_IMAGE_REQUEST);  // Gọi phương thức onActivityResult khi chọn ảnh
    }

    // Nhận kết quả từ việc chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();  // Lấy URI của ảnh
            fieldImage.setImageURI(imageUri);  // Hiển thị ảnh đã chọn trong ImageView
        }
    }

    // Phương thức tạo ID cho sân mới
    private int generateFieldId() {
        // Use current time in milliseconds as a unique ID
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }
}