package com.example.btl;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiUserInterface;
import com.example.btl.api.ApiUserService;
import com.example.btl.models.User;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword;
    private ApiUserService apiUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        // Khởi tạo ApiUserService để gọi API
        ApiUserInterface apiUserInterface = ApiClient.getClient().create(ApiUserInterface.class);
        apiUserService = new ApiUserService(apiUserInterface);

        // Sự kiện bấm nút đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> {
            String oldPassword = edtOldPassword.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();

            // Kiểm tra thông tin đầu vào
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(ChangePasswordActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới và xác nhận không khớp", Toast.LENGTH_SHORT).show();
            } else {
                // Giả sử bạn đã có ID người dùng từ Session hoặc Intent
                int userId = 47;  // Thay thế bằng ID người dùng thực tế

                // Gọi API để thay đổi mật khẩu
                apiUserService.changePassword(userId, oldPassword, newPassword, new ApiUserService.ApiCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu đã được thay đổi", Toast.LENGTH_SHORT).show();
                        finish();  // Quay lại màn hình tài khoản
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(ChangePasswordActivity.this, "Cập nhật mật khẩu thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
