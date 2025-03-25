package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.btl.activities.BookingDetailActivity;
import com.example.btl.adapters.BookingHistoryAdapter;
import com.example.btl.models.TimeSlot;
import com.example.btl.utils.BookingDatabaseHelper;

import java.util.List;

public class AccountFragment extends Fragment {

    private TextView tvUsername, tvUserInfo;
    private RecyclerView rvBookingHistory;
    private BookingDatabaseHelper databaseHelper;

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

        tvUsername.setText("Tên người dùng: Nguyễn Văn A");
        tvUserInfo.setText("Email: example@example.com\nSố điện thoại: 0381234567");

        databaseHelper = new BookingDatabaseHelper(getContext());
        databaseHelper.open();
        List<TimeSlot> historyList = databaseHelper.getBookings();

        rvBookingHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        BookingHistoryAdapter adapter = new BookingHistoryAdapter(getContext(), historyList);
        rvBookingHistory.setAdapter(adapter);

        adapter.setOnItemClickListener(slot -> {
            Intent intent = new Intent(getContext(), BookingDetailActivity.class);
            intent.putExtra("booking_detail", slot);
            startActivity(intent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) databaseHelper.close();
    }
}
