package com.example.btl;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.activities.ConfirmBookingActivity;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiTimeSlotInterface;
import com.example.btl.api.ApiTimeSlotService;
import com.example.btl.models.TimeSlot;
import com.example.btl.adapters.TimeSlotAdapter;
import com.example.btl.models.TimeslotResponseBooking;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimeSlotAdapter adapter;
    private List<TimeSlot> dailySlots = new ArrayList<>();

    // Các thành phần giao diện khác
    private ImageView fieldImage;
    private TextView fieldNameText, fieldAddressText, fieldNumberText;
    private TextView selectedDateText, dateText, totalPrice;
    private Button btnBetween, btnNext;
    private ApiTimeSlotService apiTimeSlotService;

    // Ví dụ: dùng fieldId và bookingDate cứng (có thể thay bằng dữ liệu nhận từ Intent hoặc DatePicker)
    private int fieldId = 1;
    private Date bookingDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Ánh xạ các view từ layout
        fieldImage = findViewById(R.id.fieldImage);
        fieldNameText = findViewById(R.id.fieldNameText);
        fieldAddressText = findViewById(R.id.fieldAddressText);
        fieldNumberText = findViewById(R.id.fieldNumberText);
        selectedDateText = findViewById(R.id.selectedDateText);
        dateText = findViewById(R.id.dateText);
        totalPrice = findViewById(R.id.totalPrice);
        btnBetween = findViewById(R.id.btnBetween);
        btnNext = findViewById(R.id.btnNext);

        // Trong BookingActivity.onCreate()
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("FIELD_ID")) {
            fieldId = intent.getIntExtra("FIELD_ID", 0); // Giá trị mặc định 0 nếu không có
        } else {
            // Xử lý trường hợp không có fieldId (ví dụ: hiển thị thông báo và đóng Activity)
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin sân", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Giả sử đặt ngày hiện tại (có thể thay đổi bằng DatePicker)
        bookingDate = new Date();

        // Cập nhật ngày đã chọn lên giao diện (ví dụ hiển thị dạng dd/MM/yyyy)
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDateText.setText("Ngày: " + displayDateFormat.format(bookingDate));
        selectedDateText.setOnClickListener(v -> {
            showDatePickerDialog(this);
        });
        // Khởi tạo RecyclerView (danh sách slot sẽ hiển thị theo chiều ngang)
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TimeSlotAdapter(slot -> {
            // Xử lý khi người dùng click chọn slot (chỉ cho slot AVAILABLE)
            if (slot.getStatus() == TimeSlot.AVAILABLE) {
                slot.toggleSelected();
                adapter.notifyDataSetChanged();
                // Cập nhật tổng tiền (ví dụ: mỗi slot có giá 100,000 VND)
                updateTotalPrice();
            }
        });
        recyclerView.setAdapter(adapter);

        // Khởi tạo Retrofit và ApiService
        ApiTimeSlotInterface api = ApiClient.getClient().create(ApiTimeSlotInterface.class);
        apiTimeSlotService = new ApiTimeSlotService(api);

        // Sinh ra danh sách slot trong ngày dựa trên bookingDate và fieldId
        dailySlots = generateDailyTimeSlots(bookingDate, fieldId);

        // Gọi API để lấy danh sách slot đã được đặt và cập nhật lại trạng thái
        fetchBookedTimeSlots();
        btnNext.setOnClickListener(v -> {
            // Lấy danh sách các slot đã chọn (chỉ các slot AVAILABLE)
            List<TimeSlot> selectedSlots = new ArrayList<>();
            for (TimeSlot slot : dailySlots) {
                if (slot.isSelected() && slot.getStatus() == TimeSlot.AVAILABLE) {
                    selectedSlots.add(slot);
                }
            }

            if (selectedSlots.isEmpty()) {
                Toast.makeText(BookingActivity.this, "Bạn chưa chọn khung giờ nào!", Toast.LENGTH_SHORT).show();
                return;
            }

            // --- Bổ sung: Lấy thông tin sân ---
            // Giả sử bạn có biến lưu trữ thông tin này sau khi lấy từ API hoặc nguồn khác
            String currentFieldName = fieldNameText.getText().toString(); // Lấy từ TextView đã được cập nhật
            String currentFieldAddress = fieldAddressText.getText().toString(); // Lấy từ TextView đã được cập nhật
            String currentFieldNumber = fieldNumberText.getText().toString(); // Lấy từ TextView đã được cập nhật
            // Quan trọng: Bạn cần đảm bảo fieldNameText, fieldAddressText, fieldNumberText
            // đã được cập nhật đúng thông tin của sân có fieldId hiện tại.
            // Có thể cần gọi API lấy chi tiết sân dựa vào fieldId nếu chưa có.

            // --- Bổ sung: Tính lại tổng tiền để truyền đi ---
            long currentTotalCost = 0;
            for (TimeSlot slot : selectedSlots) {
                // Sử dụng giá cố định hoặc lấy giá thực tế nếu có
                currentTotalCost += 100000; // Ví dụ giá 100,000 VND/slot
            }


            // Tạo Intent để chuyển sang ConfirmBookingActivity
            Intent intentConfirm = new Intent(BookingActivity.this, ConfirmBookingActivity.class);

            // Gửi thông tin fieldId (Đã có)
            intentConfirm.putExtra("FIELD_ID", fieldId);

            // Gửi ngày đặt (Đã có, gửi dưới dạng long)
            intentConfirm.putExtra("BOOKING_DATE", bookingDate.getTime());

            // Gửi danh sách các khung giờ đã chọn (SỬA LẠI KEY VÀ PHƯƠNG THỨC)
            // Sử dụng putParcelableArrayListExtra vì TimeSlot là Parcelable
            intentConfirm.putParcelableArrayListExtra("SELECTED_SLOTS", (ArrayList<TimeSlot>) selectedSlots);

            // --- Bổ sung: Gửi các thông tin còn thiếu ---
            intentConfirm.putExtra("TOTAL_COST", currentTotalCost);
            intentConfirm.putExtra("FIELD_NAME", currentFieldName);
            intentConfirm.putExtra("FIELD_ADDRESS", currentFieldAddress);
            intentConfirm.putExtra("FIELD_NUMBER", currentFieldNumber);


            // startActivity(intent); // Lỗi: đang dùng biến intent cũ từ onCreate
            startActivity(intentConfirm); // Sử dụng intent mới tạo
        });

    }

    /**
     * Sinh danh sách các slot trong ngày từ 00:00 đến 24:00 với khoảng cách 30 phút.
     */
    private List<TimeSlot> generateDailyTimeSlots(Date bookingDate, int fieldId) {
        List<TimeSlot> slots = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        // Sử dụng Calendar để xử lý thời gian
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bookingDate);
        // Reset giờ, phút, giây cho ngày đặt
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        int slotIdCounter = 0;
        // Tạo 48 slot (mỗi 30 phút) từ 00:00 đến 24:00
        for (int i = 0; i < 24; i++) {
            Date start = calendar.getTime();
            String startTime = timeFormat.format(start);

            // Tăng thêm 30 phút
            calendar.add(Calendar.MINUTE, 60);
            Date end = calendar.getTime();
            String endTime = timeFormat.format(end);

            // Khởi tạo slot với trạng thái AVAILABLE mặc định
            TimeSlot slot = new TimeSlot(slotIdCounter++, fieldId, bookingDate, startTime, endTime, TimeSlot.AVAILABLE);
            slots.add(slot);
        }
        return slots;
    }

    /**
     * Gọi API lấy danh sách TimeSlot đã được đặt, sau đó cập nhật danh sách slot trống.
     */
    private void fetchBookedTimeSlots() {
        // Định dạng ngày theo chuẩn của API, ví dụ "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateStr = dateFormat.format(bookingDate);

        apiTimeSlotService.getTimeSlots(fieldId, dateStr, new ApiTimeSlotService.ApiCallback<TimeslotResponseBooking>() {
            @Override
            public void onSuccess(TimeslotResponseBooking bookedSlots) {
                // Merge các slot đã đặt từ API với danh sách slot ban đầu
                mergeBookedSlots(bookedSlots);
                // Cập nhật adapter trên UI Thread
                runOnUiThread(() -> adapter.submitList(new ArrayList<>(dailySlots)));
            }

            @Override
            public void onError(Throwable t) {
                runOnUiThread(() -> {
//                    Toast.makeText(BookingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
//                    Log.e("BookingActivity", "Error fetching time slots", t);
                    // Dù API lỗi, vẫn cập nhật danh sách slot trống
                    adapter.submitList(new ArrayList<>(dailySlots));
                });
            }
        });
    }


    /**
     * So sánh các slot trống trong ngày với danh sách đã được đặt từ API và cập nhật trạng thái.
     * Nếu có sự giao nhau (overlap) thì slot được đánh dấu là BOOKED.
     */
    // Trong BookingActivity.java
    private void mergeBookedSlots(TimeslotResponseBooking bookedSlots) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        for (TimeSlot availableSlot : dailySlots) {
            availableSlot.setStatus(TimeSlot.AVAILABLE);
            for (TimeSlot bookedSlot : bookedSlots.getData()) {
                try {
                    Date availableStart = timeFormat.parse(availableSlot.getStartTime());
                    Date availableEnd = timeFormat.parse(availableSlot.getEndTime());
                    Date bookedStart = timeFormat.parse(bookedSlot.getStartTime());
                    Date bookedEnd = timeFormat.parse(bookedSlot.getEndTime());
                    if (isOverlap(availableStart, availableEnd, bookedStart, bookedEnd)) {
                        availableSlot.setStatus(TimeSlot.BOOKED);
                        break;
                    }
                } catch (ParseException e) {
                    Log.e("BookingActivity", "Lỗi định dạng thời gian: " + e.getMessage());

                    availableSlot.setStatus(TimeSlot.BOOKED);
                }
            }
        }
    }

    /**
     * Kiểm tra xem khoảng thời gian [start, end) của slot có giao với khoảng [bStart, bEnd) của slot đã đặt không.
     */
    private boolean isOverlap(Date start, Date end, Date bStart, Date bEnd) {
        return start.before(bEnd) && bStart.before(end);
    }

    /**
     * Cập nhật tổng tiền đặt sân dựa trên số slot được chọn.
     * Ví dụ: mỗi slot có giá 100.000 VND.
     */
    private void updateTotalPrice() {
        int count = 0;
        for (TimeSlot slot : dailySlots) {
            if (slot.isSelected() && slot.getStatus() == TimeSlot.AVAILABLE) {
                count++;
            }
        }
        long total = count * 100000; // Giá mỗi slot 100,000 VND
        totalPrice.setText("Tổng tiền: " + total + " VND");
    }

    private void showDatePickerDialog(Context context) {
        // Lấy ngày hiện tại làm mặc định
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Khi người dùng chọn ngày, cập nhật bookingDate
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                bookingDate = selectedDate.getTime();

                // Cập nhật giao diện hoặc thực hiện các tác vụ cần thiết sau khi chọn ngày
                updateBookingDateUI();
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateBookingDateUI() {
        // Ví dụ: cập nhật TextView hiển thị ngày đặt
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = sdf.format(bookingDate);
        selectedDateText.setText(dateString);
        // textViewBookingDate.setText(dateString);
        fetchBookedTimeSlots();
    }
}
