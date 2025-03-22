package com.example.btl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.adapters.SelectedSlotAdapter;
import com.example.btl.models.TimeSlot;
import com.example.btl.utils.BookingDatabaseHelper;

import java.util.List;

public class ConfirmBookingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SelectedSlotAdapter adapter;
    private TextView txtDate, txtTotalPrice;
    private Button btnConfirm, btnCancel;
    private List<TimeSlot> selectedSlots;
    private int totalCost;
    private BookingDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        recyclerView = findViewById(R.id.recyclerViewSelectedSlots);
        txtDate = findViewById(R.id.txtDate);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        databaseHelper = new BookingDatabaseHelper(this);
        databaseHelper.open();

        Intent intent = getIntent();
        selectedSlots = intent.getParcelableArrayListExtra("selected_slots");
        totalCost = intent.getIntExtra("total_cost", 0);

        txtDate.setText(intent.getStringExtra("selected_date"));
        txtTotalPrice.setText("Tổng tiền: " + totalCost + " VND");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectedSlotAdapter(selectedSlots);
        recyclerView.setAdapter(adapter);

        btnConfirm.setOnClickListener(v -> {
            // Lưu thông tin đặt lịch vào SQLite
            for (TimeSlot slot : selectedSlots) {
                databaseHelper.addBooking(slot, txtDate.getText().toString(), totalCost);
            }

            // Chuyển sang màn hình thành công
            Intent successIntent = new Intent(ConfirmBookingActivity.this, SuccessActivity.class);
            startActivity(successIntent);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}
