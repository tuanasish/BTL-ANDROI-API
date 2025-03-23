package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.adapters.TimeSlotAdapter;
import com.example.btl.models.TimeSlot;
import com.example.btl.utils.BookingDatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimeSlotAdapter adapter;
    private List<List<TimeSlot>> timeSlotList;
    private List<TimeSlot> selectedSlots = new ArrayList<>();

    private TextView totalPrice, fieldNameText, fieldAddressText, fieldNumberText, selectedDateText;
    private ImageView fieldImage;
    private Button btnNext;

    private String fieldName, fieldAddress, fieldNumber;
    private int fieldImageRes;
    private String selectedDate = "";

    private final int pricePerSlot = 60000;
    private int totalCost = 0;

    private BookingDatabaseHelper bookingDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Ánh xạ View
        recyclerView = findViewById(R.id.recyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        btnNext = findViewById(R.id.btnNext);
        fieldNameText = findViewById(R.id.fieldNameText);
        fieldAddressText = findViewById(R.id.fieldAddressText);
        fieldNumberText = findViewById(R.id.fieldNumberText);
        fieldImage = findViewById(R.id.fieldImage);
        selectedDateText = findViewById(R.id.selectedDateText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Mở DB
        bookingDatabaseHelper = new BookingDatabaseHelper(this);
        bookingDatabaseHelper.open();

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            fieldName = intent.getStringExtra("name");
            fieldAddress = intent.getStringExtra("address");
            fieldNumber = intent.getStringExtra("number");
            fieldImageRes = intent.getIntExtra("image", R.drawable.ic_launcher_background);

            fieldNameText.setText(fieldName);
            fieldAddressText.setText(fieldAddress);
            fieldNumberText.setText("Số điện thoại: " + fieldNumber);
            fieldImage.setImageResource(fieldImageRes);
        }

        // Bấm chọn ngày
        selectedDateText.setOnClickListener(v -> showDatePickerDialog());

        // Bấm tiếp tục
        btnNext.setOnClickListener(v -> {
            if (selectedSlots.isEmpty()) {
                totalPrice.setText("Vui lòng chọn ít nhất một khung giờ!");
                return;
            }

            if (selectedDate.isEmpty()) {
                totalPrice.setText("Vui lòng chọn ngày!");
                return;
            }

            Intent confirmIntent = new Intent(BookingActivity.this, ConfirmBookingActivity.class);
            confirmIntent.putParcelableArrayListExtra("selected_slots", new ArrayList<>(selectedSlots));
            confirmIntent.putExtra("total_cost", totalCost);
            confirmIntent.putExtra("field_name", fieldName);
            confirmIntent.putExtra("field_address", fieldAddress);
            confirmIntent.putExtra("field_number", fieldNumber);
            confirmIntent.putExtra("field_image", fieldImageRes);
            confirmIntent.putExtra("selected_date", selectedDate);

            if (!selectedSlots.isEmpty()) {
                TimeSlot firstSlot = selectedSlots.get(0);
                confirmIntent.putExtra("selected_time", firstSlot.getTime());
                confirmIntent.putExtra("selected_duration", selectedSlots.size() * 90);
            }

            startActivity(confirmIntent);
        });
    }

    // Cập nhật giá tiền khi chọn khung giờ
    @SuppressLint("SetTextI18n")
    public void updatePrice(TimeSlot timeSlot, boolean isSelected) {
        if (isSelected) {
            selectedSlots.add(timeSlot);
        } else {
            selectedSlots.remove(timeSlot);
        }
        totalCost = selectedSlots.size() * pricePerSlot;
        totalPrice.setText("Tổng tiền: " + totalCost + " VND");
    }

    // Chọn ngày → tạo danh sách slot tương ứng
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    selectedDateText.setText("Ngày chọn: " + selectedDate);

                    // Tạo lại danh sách khung giờ mới theo ngày
                    selectedSlots.clear();
                    timeSlotList = generateTimeSlots(selectedDate);
                    adapter = new TimeSlotAdapter(timeSlotList, this::updatePrice);
                    recyclerView.setAdapter(adapter);
                    totalPrice.setText("Tổng tiền: 0 VND");
                    totalCost = 0;
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Tạo danh sách slot theo ngày
    private List<List<TimeSlot>> generateTimeSlots(String selectedDate) {
        List<List<TimeSlot>> allSlots = new ArrayList<>();
        String[] times = {"5:00", "6:30", "8:00", "9:30", "11:00", "12:30", "14:00", "15:30", "17:00", "18:30", "20:00", "21:30", "23:00"};

        for (int i = 0; i < 4; i++) {
            List<TimeSlot> slotRow = new ArrayList<>();
            for (String time : times) {
                TimeSlot slot = new TimeSlot("PickelBall " + (i + 1), time, TimeSlot.AVAILABLE, 90, selectedDate);

                if (bookingDatabaseHelper.isTimeSlotBooked(selectedDate, time)) {
                    slot.setStatus(TimeSlot.LOCKED);
                }

                slotRow.add(slot);
            }
            allSlots.add(slotRow);
        }
        return allSlots;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bookingDatabaseHelper != null) {
            bookingDatabaseHelper.close();
        }
    }
}
