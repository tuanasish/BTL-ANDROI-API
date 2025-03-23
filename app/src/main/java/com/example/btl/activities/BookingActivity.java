package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.adapters.TimeSlotAdapter;
import com.example.btl.models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimeSlotAdapter adapter;
    private List<List<TimeSlot>> timeSlotList;
    private TextView totalPrice, fieldNameText, fieldAddressText, fieldNumberText, selectedDateText;
    private ImageView fieldImage;
    private Button btnNext;
    private List<TimeSlot> selectedSlots = new ArrayList<>();
    private int pricePerSlot = 60000; // Giá cố định mỗi 1h30p
    private int totalCost = 0;
    private String fieldName, fieldAddress, fieldNumber;
    private int fieldImageRes;
    private String selectedDate = ""; // Store selected date

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        btnNext = findViewById(R.id.btnNext);
        fieldNameText = findViewById(R.id.fieldNameText);
        fieldAddressText = findViewById(R.id.fieldAddressText);
        fieldNumberText = findViewById(R.id.fieldNumberText);
        fieldImage = findViewById(R.id.fieldImage);
        selectedDateText = findViewById(R.id.selectedDateText); // Add reference to the TextView for the date

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // Nhận thông tin sân lớn từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            fieldName = intent.getStringExtra("name");
            fieldAddress = intent.getStringExtra("address");
            fieldNumber = intent.getStringExtra("number");
            fieldImageRes = intent.getIntExtra("image", R.drawable.ic_launcher_background);

            // Hiển thị thông tin sân lớn
            fieldNameText.setText(fieldName);
            fieldAddressText.setText(fieldAddress);
            fieldNumberText.setText("Số điện thoại: " + fieldNumber);
            fieldImage.setImageResource(fieldImageRes);
        }

        // Tạo danh sách khung giờ cho các sân nhỏ
        timeSlotList = generateTimeSlots();
        adapter = new TimeSlotAdapter(timeSlotList, this::updatePrice);
        recyclerView.setAdapter(adapter);

        // Set a click listener to show the DatePickerDialog
        selectedDateText.setOnClickListener(v -> showDatePickerDialog());

        // Xử lý khi nhấn nút Next
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

            // Truyền thông tin sân lớn
            confirmIntent.putExtra("field_name", fieldName);
            confirmIntent.putExtra("field_address", fieldAddress);
            confirmIntent.putExtra("field_number", fieldNumber);
            confirmIntent.putExtra("field_image", fieldImageRes);

            // Truyền thông tin ngày, giờ, thời gian
            confirmIntent.putExtra("selected_date", selectedDate); // Pass the selected date
            if (!selectedSlots.isEmpty()) {
                TimeSlot firstSlot = selectedSlots.get(0);
                confirmIntent.putExtra("selected_time", firstSlot.getTime());
                confirmIntent.putExtra("selected_duration", selectedSlots.size() * 90); // 90 phút mỗi slot
            }

            startActivity(confirmIntent);
        });
    }

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

    private List<List<TimeSlot>> generateTimeSlots() {
        List<List<TimeSlot>> allSlots = new ArrayList<>();
        String[] times = {"5:00", "6:30", "8:00", "9:30", "11:00", "12:30", "14:00", "15:30", "17:00", "18:30", "20:00", "21:30", "23:00"};

        for (int i = 0; i < 4; i++) { // 4 sân nhỏ
            List<TimeSlot> slotRow = new ArrayList<>();
            for (String time : times) {
                slotRow.add(new TimeSlot("PickelBall " + (i + 1), time, TimeSlot.AVAILABLE, 90, ""));
            }
            allSlots.add(slotRow);
        }
        return allSlots;
    }

    // Show the DatePickerDialog and handle date selection
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    // Format the selected date and set it to the TextView
                    selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    selectedDateText.setText("Ngày chọn: " + selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }
}
