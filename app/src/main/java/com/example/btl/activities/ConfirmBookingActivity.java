package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.adapters.SelectedSlotAdapter;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiFieldService;
import com.example.btl.api.ApiTimeSlotInterface;
import com.example.btl.api.ApiTimeSlotService;
import com.example.btl.models.BookingRequest;
import com.example.btl.models.Field;
import com.example.btl.models.TimeSlot;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConfirmBookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SelectedSlotAdapter adapter;
    private TextView txtDate, txtTotalPrice, txtFieldName, txtFieldAddress, txtFieldNumber, txtBookedSlots;
    private Button btnConfirm, btnCancel;
    private List<TimeSlot> selectedSlots;
    private ApiTimeSlotService api;

    private int currentFieldId;
    private long bookingDateMillis;
    private long totalCostLong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        txtDate = findViewById(R.id.txtDate);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtFieldName = findViewById(R.id.txtFieldName);
        txtFieldAddress = findViewById(R.id.txtFieldAddress);
        txtFieldNumber = findViewById(R.id.txtFieldNumber);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectedSlotAdapter(); // Không truyền list
        recyclerView.setAdapter(adapter);

        ApiTimeSlotInterface apiTimeSlotInterface = ApiClient.getClient().create(ApiTimeSlotInterface.class);
        api = new ApiTimeSlotService(apiTimeSlotInterface);

        Intent intent = getIntent();

        currentFieldId = intent.getIntExtra("FIELD_ID", -1);
        bookingDateMillis = intent.getLongExtra("BOOKING_DATE", -1);
        totalCostLong = intent.getLongExtra("TOTAL_COST", 0);
        selectedSlots = intent.getParcelableArrayListExtra("SELECTED_SLOTS");

        if (selectedSlots == null) {
            selectedSlots = new ArrayList<>();
            Toast.makeText(this, "Không có khung giờ nào được chọn!", Toast.LENGTH_SHORT).show();
            finish();
        }

        adapter.setTimeSlotList(selectedSlots); // cập nhật list cho adapter

        txtTotalPrice.setText("Tổng tiền: " + totalCostLong + " VND");

        if (bookingDateMillis != -1) {
            Date bookingDate = new Date(bookingDateMillis);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            txtDate.setText("Ngày đặt: " + dateFormat.format(bookingDate));
        }

        loadFieldById(currentFieldId); // Gọi API để lấy tên sân, địa chỉ, capacity

        StringBuilder bookingDetails = new StringBuilder();
        if (!selectedSlots.isEmpty()) {
            List<String> timeRanges = new ArrayList<>();
            for (TimeSlot slot : selectedSlots) {
                timeRanges.add(slot.getTimeRange());
            }
            bookingDetails.append(String.join(", ", timeRanges));
        } else {
            bookingDetails.append("Không có khung giờ nào được chọn.");
        }

        btnConfirm.setOnClickListener(v -> {
            int userId = 47;
            int courtId = 1;
            double totalPrice = intent.getDoubleExtra("TOTAL_PRICE", totalCostLong);

            if (selectedSlots.isEmpty() || currentFieldId == -1) {
                Toast.makeText(this, "Lỗi dữ liệu đặt sân!", Toast.LENGTH_SHORT).show();
                return;
            }

            TimeSlot slot = selectedSlots.get(0);
            String startTime = slot.getStartTime();
            String endTime = slot.getEndTime();

            String bookingDateFormatted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date(bookingDateMillis));

            BookingRequest request = new BookingRequest(
                    currentFieldId,
                    bookingDateFormatted,
                    startTime,
                    endTime,
                    userId,
                    courtId,
                    totalPrice
            );

            Log.d("BookingDebug", "FieldId: " + currentFieldId);
            Log.d("BookingDebug", "Date: " + bookingDateFormatted);
            Log.d("BookingDebug", "Start: " + startTime);
            Log.d("BookingDebug", "End: " + endTime);
            Log.d("BookingDebug", "UserId: " + userId);
            Log.d("BookingDebug", "CourtId: " + courtId);
            Log.d("BookingDebug", "TotalPrice: " + totalPrice);

            api.createBooking(request, new ApiTimeSlotService.ApiCallback<JsonObject>() {
                @Override
                public void onSuccess(JsonObject result) {
                    boolean success = result.has("message") && result.get("message").getAsBoolean();

                    if (success) {
                        Toast.makeText(ConfirmBookingActivity.this, "Đặt sân thành công!", Toast.LENGTH_SHORT).show();
                        Intent successIntent = new Intent(ConfirmBookingActivity.this, SuccessActivity.class);
                        startActivity(successIntent);
                        finish();
                    } else {
                        Toast.makeText(ConfirmBookingActivity.this, "Đã xảy ra lỗi khi lưu thông tin đặt sân!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(ConfirmBookingActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        btnCancel.setOnClickListener(v -> finish());
    }

    private void loadFieldById(int fieldId) {
        ApiFieldInterface apiFieldInterface = ApiClient.getClient().create(ApiFieldInterface.class);
        ApiFieldService apiFieldService = new ApiFieldService(apiFieldInterface);

        apiFieldService.getFieldById(fieldId, new ApiFieldService.ApiCallback<Field>() {
            @Override
            public void onSuccess(Field field) {
                txtFieldName.setText(field.getName());
                txtFieldAddress.setText(field.getLocation());
                txtFieldNumber.setText(field.getCapacity() + " người");
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(ConfirmBookingActivity.this, "Không thể tải thông tin sân", Toast.LENGTH_SHORT).show();
                Log.e("ConfirmBookingActivity", "API ERROR: " + t.getMessage());
            }
        });
    }
}
