package com.example.btl.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.adapters.SelectedSlotAdapter;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiTimeSlotInterface;
import com.example.btl.api.ApiTimeSlotService;
import com.example.btl.models.BookingRequest;
import com.example.btl.models.TimeSlot;
import com.example.btl.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConfirmBookingActivity extends AppCompatActivity {
    private TextView txtDate, txtTotalPrice, txtFieldName, txtFieldAddress, txtFieldNumber, txtBookedSlots;
    private Button btnConfirm, btnCancel;
    private List<TimeSlot> selectedSlots;
    private ApiTimeSlotService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        // Ánh xạ Views
        txtDate = findViewById(R.id.txtDate);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtFieldName = findViewById(R.id.txtFieldName);
        txtFieldAddress = findViewById(R.id.txtFieldAddress);
        txtFieldNumber = findViewById(R.id.txtFieldNumber);
        txtBookedSlots = findViewById(R.id.txtBookedSlots);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        // Gọi API
        ApiTimeSlotInterface apiTimeSlotInterface = ApiClient.getClient().create(ApiTimeSlotInterface.class);
        api = new ApiTimeSlotService(apiTimeSlotInterface);

        // Lấy user từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("USER_PREF", MODE_PRIVATE);
        String userJson = prefs.getString("USER_DATA", null);

        if (userJson == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Gson gson = new Gson();
        final User user = gson.fromJson(userJson, User.class);
        final int userId = user.getUser_id(); // dùng trong lambda

        // Lấy dữ liệu từ intent
        Intent intent = getIntent();
        selectedSlots = intent.getParcelableArrayListExtra("SELECTED_SLOTS");
        if (selectedSlots == null || selectedSlots.isEmpty()) {
            Toast.makeText(this, "Không có khung giờ nào được chọn!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        long totalCostLong = intent.getLongExtra("TOTAL_COST", 0);
        txtTotalPrice.setText("Tổng tiền: " + totalCostLong + " VND");

        long bookingDateMillis = intent.getLongExtra("BOOKING_DATE", -1);
        final String bookingDateFormatted = (bookingDateMillis != -1)
                ? new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(bookingDateMillis))
                : "Không xác định";

        txtDate.setText("Ngày đặt: " + bookingDateFormatted);

        String fieldName = intent.getStringExtra("FIELD_NAME");
        String fieldAddress = intent.getStringExtra("FIELD_ADDRESS");
        String fieldNumber = intent.getStringExtra("FIELD_NUMBER");
        int currentFieldId = intent.getIntExtra("FIELD_ID", -1);

        txtFieldName.setText(fieldName != null ? fieldName : "N/A");
        txtFieldAddress.setText(fieldAddress != null ? fieldAddress : "N/A");
        txtFieldNumber.setText(fieldNumber != null ? fieldNumber : "N/A");

        // Hiển thị chi tiết đặt
        StringBuilder bookingDetails = new StringBuilder();
        bookingDetails.append(fieldName != null ? fieldName + ": " : "");
        List<String> timeRanges = new ArrayList<>();
        for (TimeSlot slot : selectedSlots) {
            timeRanges.add(slot.getTimeRange());
        }
        bookingDetails.append(String.join(", ", timeRanges));
        txtBookedSlots.setText(bookingDetails.toString());

        // Xử lý nút xác nhận
        btnConfirm.setOnClickListener(v -> {
            int courtId = 1; // tạm thời cố định
            double totalPrice = intent.getDoubleExtra("TOTAL_PRICE", totalCostLong);

            TimeSlot slot = selectedSlots.get(0); // lấy slot đầu
            String startTime = slot.getStartTime();
            String endTime = slot.getEndTime();

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
                        startActivity(new Intent(ConfirmBookingActivity.this, SuccessActivity.class));
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
}
