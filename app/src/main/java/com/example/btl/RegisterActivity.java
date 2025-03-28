package com.example.btl;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiUserInterface;
import com.example.btl.api.ApiUserService;
import com.example.btl.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvToLogin;
    private ApiUserService apiUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ánh xạ view
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone); // Thêm ánh xạ cho số điện thoại
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvToLogin = findViewById(R.id.tvToLogin);

        // Khởi tạo API service
        ApiUserInterface apiUserInterface = ApiClient.getClient().create(ApiUserInterface.class);
        apiUserService = new ApiUserService(apiUserInterface);

        // Xử lý sự kiện đăng ký
        btnRegister.setOnClickListener(v -> handleRegister());

        // Xử lý chuyển sang màn hình đăng nhập
        tvToLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void handleRegister() {
        // Lấy dữ liệu từ các trường nhập
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        // Validate dữ liệu
        if (username.isEmpty()) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            return;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return;
        }

        if (phone.isEmpty() || phone.length() < 10) { // Validate số điện thoại
            etPhone.setError("Số điện thoại không hợp lệ");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu không khớp");
            return;
        }

        // Tạo đối tượng User từ thông tin đăng ký
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setPassword(password);

        // Gọi API đăng ký
        apiUserService.registerUser(newUser, new ApiUserService.ApiCallback<User>() {
            @Override
            public void onSuccess(User result) {
                // Xử lý khi đăng ký thành công
                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                // Chuyển về màn hình đăng nhập sau khi đăng ký thành công
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onError(Throwable t) {
                // Xử lý khi có lỗi
                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}