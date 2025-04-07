package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.ChangePasswordActivity;
import com.example.btl.EditInfoActivity;
import com.example.btl.LoginActivity;
import com.example.btl.R;
import com.example.btl.models.User;
import com.example.btl.utils.BookingDatabaseHelper;

public class AdminAccountFragment extends Fragment {

    private TextView tvUsername, tvUserInfo;
    private User loginUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_admin, container, false);
        setHasOptionsMenu(true);

        tvUsername = view.findViewById(R.id.tvUsername);
        tvUserInfo = view.findViewById(R.id.tvUserInfo);

        // Lấy dữ liệu user từ arguments
        if (getArguments() != null) {
            loginUser = (User) getArguments().getSerializable("USER_DATA");
            if (loginUser != null) {
                tvUsername.setText("Tên người dùng: " + loginUser.getUsername());
                tvUserInfo.setText("Email: " + loginUser.getEmail() + "\nSố điện thoại: " + loginUser.getPhone());
            }
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.account_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_edit_info) {
            startActivity(new Intent(getActivity(), EditInfoActivity.class));
            return true;
        } else if (id == R.id.menu_change_password) {
            startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            return true;
        } else if (id == R.id.menu_logout) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
