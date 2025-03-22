package com.example.btl;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditInfoActivity extends AppCompatActivity {

    private EditText edtFullName, edtEmail, edtPhone;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnSave = findViewById(R.id.btnSave);

        // Sự kiện bấm nút lưu thông tin
        btnSave.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString();
            String email = edtEmail.getText().toString();
            String phone = edtPhone.getText().toString();

            // Lưu thông tin (có thể lưu vào cơ sở dữ liệu)
            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(EditInfoActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Thông báo thành công
                Toast.makeText(EditInfoActivity.this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                // Quay lại màn hình tài khoản
                finish();
            }
        });
    }
}
