package com.example.btl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.models.User;
import com.example.btl.fragments.UserManagementFragment;

import java.sql.Timestamp;

public class AddUserActivity extends AppCompatActivity {

    private EditText edtUsername, edtEmail, edtPhone, edtRole;
    private Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Ánh xạ các phần tử
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtRole = findViewById(R.id.edtRole);
        btnAddUser = findViewById(R.id.btnAddUser);

        // Xử lý sự kiện khi nhấn nút Thêm Người Dùng
        btnAddUser.setOnClickListener(v -> {
            String username = edtUsername.getText().toString();
            String email = edtEmail.getText().toString();
            String phone = edtPhone.getText().toString();
            String role = edtRole.getText().toString();

            // Kiểm tra các trường có hợp lệ không
            if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || role.isEmpty()) {
                Toast.makeText(AddUserActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng User mới
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setRole(role);
            newUser.setCreate_at(new Timestamp(System.currentTimeMillis()));
            newUser.setUpdate_at(new Timestamp(System.currentTimeMillis()));

            // Thêm người dùng vào danh sách trong UserManagementFragment
            UserManagementFragment fragment = (UserManagementFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                fragment.addUser(newUser);
            }

            // Thông báo thành công
            Toast.makeText(AddUserActivity.this, "Người dùng đã được thêm", Toast.LENGTH_SHORT).show();

            // Quay lại màn hình quản lý người dùng
            finish();  // Đóng Activity và quay lại UserManagementFragment
        });
    }
}
