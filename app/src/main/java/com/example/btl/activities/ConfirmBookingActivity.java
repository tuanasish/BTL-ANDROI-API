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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmBookingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SelectedSlotAdapter adapter;
    private TextView txtDate, txtTotalPrice, txtFieldName, txtFieldAddress, txtFieldNumber, txtBookedSlots;
    private Button btnConfirm, btnCancel;
    private List<TimeSlot> selectedSlots;
    private int totalCost;
    private BookingDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        txtDate = findViewById(R.id.txtDate);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtFieldName = findViewById(R.id.txtFieldName);
        txtFieldAddress = findViewById(R.id.txtFieldAddress);
        txtFieldNumber = findViewById(R.id.txtFieldNumber);
        txtBookedSlots = findViewById(R.id.txtBookedSlots);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        databaseHelper = new BookingDatabaseHelper(this);
        databaseHelper.open();

        Intent intent = getIntent();
        selectedSlots = intent.getParcelableArrayListExtra("selected_slots");
        totalCost = intent.getIntExtra("total_cost", 0);

        txtDate.setText(intent.getStringExtra("selected_date"));
        txtTotalPrice.setText("Tổng tiền: " + totalCost + " VND");

        String fieldName = intent.getStringExtra("field_name");
        String fieldAddress = intent.getStringExtra("field_address");
        String fieldNumber = intent.getStringExtra("field_number");

        txtFieldName.setText(fieldName);
        txtFieldAddress.setText(fieldAddress);
        txtFieldNumber.setText("Số điện thoại: " + fieldNumber);

        Map<String, List<String>> groupedSlots = new HashMap<>();
        for (TimeSlot slot : selectedSlots) {
            groupedSlots.putIfAbsent(slot.getFieldName(), new ArrayList<>());
            groupedSlots.get(slot.getFieldName()).add(slot.getTime());
        }

        StringBuilder bookingDetails = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : groupedSlots.entrySet()) {
            bookingDetails.append(entry.getKey()).append(": ");
            bookingDetails.append(String.join(", ", entry.getValue())).append("\n");
        }
        txtBookedSlots.setText(bookingDetails.toString().trim());

        btnConfirm.setOnClickListener(v -> {
            String bookedDate = txtDate.getText().toString();

            // Gán thêm địa chỉ và số điện thoại vào từng slot
            for (TimeSlot slot : selectedSlots) {
                slot.setFieldAddress(fieldAddress);
                slot.setFieldNumber(fieldNumber);
                databaseHelper.addBooking(slot, bookedDate, totalCost);
            }

            startActivity(new Intent(ConfirmBookingActivity.this, SuccessActivity.class));
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