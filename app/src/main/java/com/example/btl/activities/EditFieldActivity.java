package com.example.btl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btl.R;
import com.example.btl.models.Field;

public class EditFieldActivity extends AppCompatActivity {

    private EditText fieldName, fieldLocation, fieldPrice, fieldCapacity, fieldDescription;
    private Button btnSaveChanges;
    private Field field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_field);

        // Ánh xạ các phần tử
        fieldName = findViewById(R.id.fieldName);
        fieldLocation = findViewById(R.id.fieldLocation);
        fieldPrice = findViewById(R.id.fieldPrice);
        fieldCapacity = findViewById(R.id.fieldCapacity);
        fieldDescription = findViewById(R.id.fieldDescription);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Lấy đối tượng Field từ Intent
        field = (Field) getIntent().getSerializableExtra("FIELD");

        // Thiết lập giá trị cho các trường nhập liệu
        if (field != null) {
            fieldName.setText(field.getName());
            fieldLocation.setText(field.getLocation());
            fieldPrice.setText(String.valueOf(field.getPrice()));
            fieldCapacity.setText(String.valueOf(field.getCapacity()));
            fieldDescription.setText(field.getDescription());
        }

        // Lưu thay đổi khi nhấn nút Save
        btnSaveChanges.setOnClickListener(v -> {
            String name = fieldName.getText().toString();
            String location = fieldLocation.getText().toString();
            double price = Double.parseDouble(fieldPrice.getText().toString());
            int capacity = Integer.parseInt(fieldCapacity.getText().toString());
            String description = fieldDescription.getText().toString();

            // Cập nhật đối tượng Field
            field.setName(name);
            field.setLocation(location);
            field.setPrice(price);
            field.setCapacity(capacity);
            field.setDescription(description);

            // Thông báo cho người dùng
            Toast.makeText(EditFieldActivity.this, "Thông tin sân đã được cập nhật", Toast.LENGTH_SHORT).show();

            // Trả lại thông tin đã chỉnh sửa cho ManageFieldsFragment
            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_FIELD", field);
            setResult(RESULT_OK, resultIntent);

            // Quay lại ManageFieldsFragment
            finish();
        });
    }
}
