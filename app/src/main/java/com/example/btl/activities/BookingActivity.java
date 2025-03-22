package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
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
    private TextView totalPrice;
    private Button btnNext;
    private List<TimeSlot> selectedSlots = new ArrayList<>();
    private int pricePerSlot = 60000; // Giá cố định cho mỗi 1h30p
    private int totalCost = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        recyclerView = findViewById(R.id.recyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        btnNext = findViewById(R.id.btnNext);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        timeSlotList = generateTimeSlots();

                adapter = new TimeSlotAdapter(timeSlotList, this::updatePrice);
//        adapter = new TimeSlotAdapter(null, this::updatePrice);

        recyclerView.setAdapter(adapter);

        // Xử lý khi nhấn nút Next
        btnNext.setOnClickListener(v -> {
            if (selectedSlots.isEmpty()) {
                totalPrice.setText("Vui lòng chọn ít nhất một khung giờ!");
                return;
            }

            Intent intent = new Intent(BookingActivity.this, ConfirmBookingActivity.class);
            intent.putParcelableArrayListExtra("selected_slots", new ArrayList<>(selectedSlots));
            intent.putExtra("total_cost", totalCost);

            // Truyền thêm thông tin Sân, Ngày, Giờ, Thời gian
            if (!selectedSlots.isEmpty()) {
                TimeSlot firstSlot = selectedSlots.get(0);
                intent.putExtra("field_name", firstSlot.getFieldName());
                intent.putExtra("selected_date", "11/03/2025"); // TODO: Lấy ngày thực tế từ lịch
                intent.putExtra("selected_time", firstSlot.getTime());
                intent.putExtra("selected_duration", selectedSlots.size() * 90); // 90 phút mỗi slot
            }

            startActivity(intent);
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

        for (int i = 0; i < 4; i++) { // 4 sân
            List<TimeSlot> slotRow = new ArrayList<>();
            for (String time : times) {
                slotRow.add(new TimeSlot("Pickleball " + (i + 1), time, TimeSlot.AVAILABLE, 90, ""));

            }
            allSlots.add(slotRow);
        }
        return allSlots;
    }
}
