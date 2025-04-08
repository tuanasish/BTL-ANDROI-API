package com.example.btl.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
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
import com.example.btl.adapters.BookingHistoryAdapter;
import com.example.btl.api.ApiBookingInterface;
import com.example.btl.api.ApiClient;
import com.example.btl.models.BookingResponse;
import com.example.btl.models.User;
import com.example.btl.utils.BookingDatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private TextView tvUsername, tvUserInfo;
    private RecyclerView rvBookingHistory;
    private BookingDatabaseHelper databaseHelper;
    private User loginUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        setHasOptionsMenu(true);

        tvUsername = view.findViewById(R.id.tvUsername);
        tvUserInfo = view.findViewById(R.id.tvUserInfo);
        rvBookingHistory = view.findViewById(R.id.rvBookingHistory);

        if (getArguments() != null) {
            loginUser = (User) getArguments().getSerializable("USER_DATA");
        }
        if (loginUser == null) {
            SharedPreferences prefs = getContext().getSharedPreferences("USER_PREF", Context.MODE_PRIVATE);
            String userJson = prefs.getString("USER_DATA", null);
            if (userJson != null) {
                loginUser = new Gson().fromJson(userJson, User.class);
            }
        }
        if (loginUser != null) {
            tvUsername.setText("Tên người dùng: " + loginUser.getUsername());
            tvUserInfo.setText("Email: " + loginUser.getEmail() + "\nSố điện thoại: " + loginUser.getPhone());
        } else {
            tvUsername.setText("Không thể tải thông tin người dùng");
            tvUserInfo.setText("");
        }

        databaseHelper = new BookingDatabaseHelper(getContext());
        databaseHelper.open();
        rvBookingHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        ApiBookingInterface api = ApiClient.getClient().create(ApiBookingInterface.class);
        Call<JsonObject> call = api.getBookingsWithField(loginUser.getUser_id());

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement dataElement = response.body().get("data");

                    if (dataElement != null && dataElement.isJsonArray()) {
                        JsonArray dataArray = dataElement.getAsJsonArray();
                        List<BookingResponse> bookingList = new ArrayList<>();
                        Gson gson = new Gson();

                        for (JsonElement element : dataArray) {
                            BookingResponse booking = gson.fromJson(element, BookingResponse.class);
                            bookingList.add(booking);
                        }

                        BookingHistoryAdapter adapter = new BookingHistoryAdapter(getContext(), bookingList);
                        rvBookingHistory.setAdapter(adapter);
                    } else {
                        Log.e("API_RESPONSE", "data = null hoặc không phải JsonArray");
                    }
                } else {
                    Log.e("API_ERROR", "Dữ liệu trả về không hợp lệ");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối: " + t.getMessage());
            }
        });

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
            if (loginUser != null) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra("USER_DATA", loginUser);
                startActivity(intent);
            }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) databaseHelper.close();
    }
}
