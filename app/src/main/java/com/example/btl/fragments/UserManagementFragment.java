package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.btl.R;
import com.example.btl.activities.AddUserActivity;
import com.example.btl.adapters.UserManagerAdapter;
import com.example.btl.models.User;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserManagementFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserManagerAdapter userManagerAdapter;
    private List<User> userList;
    private Button btnAddUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_management, container, false);

        // Ánh xạ RecyclerView và nút "Thêm Người Dùng"
        recyclerView = view.findViewById(R.id.recyclerViewUsers);
        btnAddUser = view.findViewById(R.id.btnAddUser);

        // Khởi tạo danh sách người dùng
        userList = getSampleUsers();  // Lấy dữ liệu mẫu

        // Khởi tạo adapter với dữ liệu mẫu
        userManagerAdapter = new UserManagerAdapter(getContext(), userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(userManagerAdapter);

        // Sự kiện nhấn nút "Thêm Người Dùng"
        btnAddUser.setOnClickListener(v -> {
            // Mở AddUserActivity để thêm người dùng
            Intent intent = new Intent(getActivity(), AddUserActivity.class);
            startActivity(intent);
        });

        return view;
    }

    // Phương thức để lấy dữ liệu mẫu
    private List<User> getSampleUsers() {
        List<User> users = new ArrayList<>();

        // Dữ liệu mẫu 1
        User user1 = new User();
        user1.setUser_id(1);
        user1.setUsername("NguyenVanA");
        user1.setEmail("nguyenvana@example.com");
        user1.setPhone("0901234567");
        user1.setRole("Admin");
        user1.setPassword("password123");
        user1.setCreate_at(new Timestamp(System.currentTimeMillis()));
        user1.setUpdate_at(new Timestamp(System.currentTimeMillis()));
        users.add(user1);

        // Dữ liệu mẫu 2
        User user2 = new User();
        user2.setUser_id(2);
        user2.setUsername("TranThiB");
        user2.setEmail("tranthib@example.com");
        user2.setPhone("0912345678");
        user2.setRole("User");
        user2.setPassword("password456");
        user2.setCreate_at(new Timestamp(System.currentTimeMillis()));
        user2.setUpdate_at(new Timestamp(System.currentTimeMillis()));
        users.add(user2);

        // Dữ liệu mẫu 3
        User user3 = new User();
        user3.setUser_id(3);
        user3.setUsername("PhamMinhC");
        user3.setEmail("phamminhc@example.com");
        user3.setPhone("0923456789");
        user3.setRole("Manager");
        user3.setPassword("password789");
        user3.setCreate_at(new Timestamp(System.currentTimeMillis()));
        user3.setUpdate_at(new Timestamp(System.currentTimeMillis()));
        users.add(user3);

        return users;
    }

    // Phương thức để thêm người dùng vào danh sách
    public void addUser(User newUser) {
        if (newUser != null) {
            userList.add(newUser);
            // Cập nhật RecyclerView
            userManagerAdapter.notifyDataSetChanged();
        }
    }
}
