package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.btl.LoginActivity;
import com.example.btl.R;
import com.example.btl.EditInfoActivity;
import com.example.btl.ChangePasswordActivity;

public class AccountFragment extends Fragment {

    private TextView tvUsername, tvUserInfo;
    private Button btnEditInfo, btnChangePassword, btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Ánh xạ view
        tvUsername = view.findViewById(R.id.tvUsername);
        tvUserInfo = view.findViewById(R.id.tvUserInfo);
        btnEditInfo = view.findViewById(R.id.btnEditInfo);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Hiển thị tên người dùng và thông tin
        tvUsername.setText("Tên người dùng: Nguyễn Văn A");
        tvUserInfo.setText("Email: example@example.com\nSố điện thoại: 0381234567");

        // Sự kiện bấm nút chỉnh sửa thông tin
        btnEditInfo.setOnClickListener(v -> {
            Intent editIntent = new Intent(getActivity(), EditInfoActivity.class);
            startActivity(editIntent);
        });

        // Sự kiện bấm nút đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> {
            Intent changePasswordIntent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivity(changePasswordIntent);
        });

        // Sự kiện bấm nút đăng xuất
        btnLogout.setOnClickListener(v -> {
            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
            startActivity(loginIntent);
            getActivity().finish();
        });

        return view;
    }
}
