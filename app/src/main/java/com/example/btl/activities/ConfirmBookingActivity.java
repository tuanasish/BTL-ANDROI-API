package com.example.btl.activities;

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
import com.example.btl.api.ApiTimeSlotInterface;
import com.example.btl.api.ApiTimeSlotService;
import com.example.btl.models.BookingRequest;
import com.example.btl.models.TimeSlot;
import com.example.btl.utils.BookingDatabaseHelper;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfirmBookingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SelectedSlotAdapter adapter;
    private TextView txtDate, txtTotalPrice, txtFieldName, txtFieldAddress, txtFieldNumber, txtBookedSlots;
    private Button btnConfirm, btnCancel;
    private List<TimeSlot> selectedSlots;
    private int totalCost;
    private ApiTimeSlotService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        // Ánh xạ Views... (Giữ nguyên)
        txtDate = findViewById(R.id.txtDate);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtFieldName = findViewById(R.id.txtFieldName);
        txtFieldAddress = findViewById(R.id.txtFieldAddress);
        txtFieldNumber = findViewById(R.id.txtFieldNumber);
        txtBookedSlots = findViewById(R.id.txtBookedSlots);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);

        ApiTimeSlotInterface apiTimeSlotInterface = ApiClient.getClient().create(ApiTimeSlotInterface.class);
        api = new ApiTimeSlotService(apiTimeSlotInterface);


        Intent intent = getIntent();

        // --- Sửa lỗi nhận dữ liệu ---
        // Nhận danh sách slot (dùng getParcelableArrayListExtra và đúng key)
        selectedSlots = intent.getParcelableArrayListExtra("SELECTED_SLOTS");
        if (selectedSlots == null) {
            selectedSlots = new ArrayList<>();
            finish();
        }


        // Nhận tổng tiền (dùng getLongExtra và đúng key)
        // Sửa lại totalCost thành kiểu long nếu giá trị có thể lớn
        long totalCostLong = intent.getLongExtra("TOTAL_COST", 0);
        txtTotalPrice.setText("Tổng tiền: " + totalCostLong + " VND");


        // Nhận ngày đặt (dùng getLongExtra, chuyển về Date/String)
        long bookingDateMillis = intent.getLongExtra("BOOKING_DATE", -1);
        String selectedDateStr = "Không xác định";
        if (bookingDateMillis != -1) {
            Date bookingDate = new Date(bookingDateMillis);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            selectedDateStr = dateFormat.format(bookingDate);
        }
        txtDate.setText("Ngày đặt: " + selectedDateStr); // Cập nhật TextView ngày


        // Nhận thông tin sân (dùng getStringExtra và đúng key)
        String fieldName = intent.getStringExtra("FIELD_NAME");
        String fieldAddress = intent.getStringExtra("FIELD_ADDRESS");
        String fieldNumber = intent.getStringExtra("FIELD_NUMBER");


        // --- Cập nhật hiển thị thông tin sân ---
        txtFieldName.setText(fieldName != null ? fieldName : "N/A");
        txtFieldAddress.setText(fieldAddress != null ? fieldAddress : "N/A");
        // Giả sử fieldNumber từ Intent đã có tiền tố "Số điện thoại: "
        // Nếu không, bạn cần thêm vào đây:
        // txtFieldNumber.setText("Số điện thoại: " + (fieldNumber != null ? fieldNumber : "N/A"));
        txtFieldNumber.setText(fieldNumber != null ? fieldNumber : "N/A"); // Nếu đã có tiền tố

        StringBuilder bookingDetails = new StringBuilder();
        if (fieldName != null && !selectedSlots.isEmpty()) {
            bookingDetails.append(fieldName).append(": ");
            List<String> timeRanges = new ArrayList<>();
            for (TimeSlot slot : selectedSlots) {
                timeRanges.add(slot.getTimeRange()); // Sử dụng hàm getTimeRange() đã có
            }
            bookingDetails.append(String.join(", ", timeRanges));
        } else {
            bookingDetails.append("Không có khung giờ nào được chọn.");
        }
        txtBookedSlots.setText(bookingDetails.toString());


        // --- Xử lý nút Xác nhận ---

        btnConfirm.setOnClickListener(v -> {
            int currentFieldId = intent.getIntExtra("FIELD_ID", -1);
            int userId = 47;
            int courtId = 1;
            double totalPrice = intent.getDoubleExtra("TOTAL_PRICE", totalCostLong);

            if (selectedSlots == null || selectedSlots.isEmpty() || currentFieldId == -1 || userId == -1 || courtId == -1) {
                Toast.makeText(this, "Lỗi dữ liệu đặt sân!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Chỉ lấy slot đầu tiên làm ví dụ để gửi (tùy backend hỗ trợ gộp hay từng cái)
            TimeSlot slot = selectedSlots.get(0);
            String startTime = slot.getStartTime(); // Giả sử TimeSlot có getStartTime()
            String endTime = slot.getEndTime();     // Giả sử TimeSlot có getEndTime()

            // Convert ngày từ milliseconds về định dạng yyyy-MM-dd
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
//            BookingRequest request = new BookingRequest(
//                    3,
//                    "2025-04-10",
//                    "10:00:00",
//                    "11:00:00",
//                    1,
//                    1,
//                    100.50
//            );
            Log.d("BookingDebug", "FieldId: " + currentFieldId);
            Log.d("BookingDebug", "Date: " + bookingDateFormatted);
            Log.d("BookingDebug", "Start: " + startTime);
            Log.d("BookingDebug", "End: " + endTime);
            Log.d("BookingDebug", "UserId: " + userId);
            Log.d("BookingDebug", "CourtId: " + courtId);
            Log.d("BookingDebug", "TotalPrice: " + totalPrice);

            // Gọi API tạo booking
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}