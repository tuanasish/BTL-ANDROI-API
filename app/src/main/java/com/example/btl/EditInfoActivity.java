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

public class EditInfoActivity extends AppCompatActivity {

    private EditText edtFullName, edtEmail, edtPhone;
    private Button btnSave;
    private ApiUserService apiUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        btnSave = findViewById(R.id.btnSave);

        // Khởi tạo ApiUserService để gọi API
        ApiUserInterface apiUserInterface = ApiClient.getClient().create(ApiUserInterface.class);
        apiUserService = new ApiUserService(apiUserInterface);

        // Nhận dữ liệu người dùng từ Intent
        User currentUser = (User) getIntent().getSerializableExtra("USER_DATA");

        // Kiểm tra xem dữ liệu có hợp lệ không
        if (currentUser != null) {
            // Hiển thị thông tin người dùng vào các EditText
            edtFullName.setText(currentUser.getUsername());
            edtEmail.setText(currentUser.getEmail());
            edtPhone.setText(currentUser.getPhone());
        }

        // Sự kiện bấm nút lưu thông tin
        btnSave.setOnClickListener(v -> {
            String fullName = edtFullName.getText().toString();
            String email = edtEmail.getText().toString();
            String phone = edtPhone.getText().toString();

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                Toast.makeText(EditInfoActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                // Cập nhật thông tin người dùng
                User updatedUser = new User();
                updatedUser.setUsername(fullName);
                updatedUser.setEmail(email);
                updatedUser.setPhone(phone);

                // Gọi API để cập nhật thông tin người dùng
                // Sửa lại payload với user_id đúng
                apiUserService.updateUser(currentUser.getUser_id(), updatedUser, new ApiUserService.ApiCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        Toast.makeText(EditInfoActivity.this, "Thông tin đã được cập nhật", Toast.LENGTH_SHORT).show();
                        finish();  // Quay lại màn hình tài khoản
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(EditInfoActivity.this, "Cập nhật thất bại: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
