package com.example.btl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiUserInterface;
import com.example.btl.api.ApiUserService;
import com.example.btl.models.User;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText loginEmail, loginPassword;
    private Button btnLogin;
    private TextView tvToRegister;
    private ApiUserService apiUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ view
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvToRegister = findViewById(R.id.tvToRegister);

        ApiUserInterface apiUserInterface = ApiClient.getClient().create(ApiUserInterface.class);
        apiUserService = new ApiUserService(apiUserInterface);
        // Xử lý sự kiện đăng nhập
        btnLogin.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            } /*else {
                Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }*/
            apiUserService.loginUser(email, password, new ApiUserService.ApiCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    Toast.makeText(LoginActivity.this,
                            "Xin Chào : " + user.getUsername(),
                            Toast.LENGTH_SHORT).show();
                    Log.d("LOGIN_SUCCESS", "User data: " + user.toString());
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    // lấy dữ liệu của người dùng khi đăng nhập
                    intent.putExtra("USER_DATA", user);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            });

        });

        // Chuyển sang màn hình đăng ký
        tvToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
