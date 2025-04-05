package com.example.btl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.models.User;

public class EditUserActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPhone, edtPassword, edtRole;
    private Button btnSaveChanges;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Ánh xạ các phần tử
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtRole = findViewById(R.id.edtRole);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Lấy thông tin người dùng từ Intent
        currentUser = (User) getIntent().getSerializableExtra("USER");

        if (currentUser != null) {
            // Điền các trường với thông tin người dùng hiện tại
            edtUsername.setText(currentUser.getUsername());
            edtEmail.setText(currentUser.getEmail());
            edtPhone.setText(currentUser.getPhone());
            edtPassword.setText(currentUser.getPassword());
            edtRole.setText(currentUser.getRole());
        }

        // Lưu thay đổi khi nhấn nút Lưu
        btnSaveChanges.setOnClickListener(v -> {
            // Cập nhật thông tin người dùng
            currentUser.setUsername(edtUsername.getText().toString());
            currentUser.setEmail(edtEmail.getText().toString());
            currentUser.setPhone(edtPhone.getText().toString());
            currentUser.setPassword(edtPassword.getText().toString());
            currentUser.setRole(edtRole.getText().toString());

            // Gửi thông tin đã chỉnh sửa về lại UserManagementFragment (hoặc Activity chứa fragment)
            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_USER", currentUser);
            setResult(RESULT_OK, resultIntent);  // Trả lại kết quả cho UserManagementFragment hoặc Activity
            finish();  // Đóng Activity và quay lại màn hình quản lý người dùng
        });

    }
}
