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

import com.bumptech.glide.Glide;
import com.example.btl.activities.ConfirmBookingActivity;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiFieldService;
import com.example.btl.api.ApiTimeSlotInterface;
import com.example.btl.api.ApiTimeSlotService;
import com.example.btl.models.Field;
import com.example.btl.models.TimeSlot;
import com.example.btl.adapters.TimeSlotAdapter;
import com.example.btl.models.TimeslotResponseBooking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimeSlotAdapter adapter;
    private List<TimeSlot> dailySlots = new ArrayList<>();

    private ImageView fieldImage;
    private TextView fieldNameText, fieldAddressText, fieldNumberText;
    private TextView selectedDateText, dateText, totalPrice;
    private Button btnBetween, btnNext;
    private ApiTimeSlotService apiTimeSlotService;

    private int fieldId = 1;
    private Date bookingDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        fieldImage = findViewById(R.id.fieldImage);
        fieldNameText = findViewById(R.id.fieldNameText);
        fieldAddressText = findViewById(R.id.fieldAddressText);
        fieldNumberText = findViewById(R.id.fieldNumberText);
        selectedDateText = findViewById(R.id.selectedDateText);
        dateText = findViewById(R.id.dateText);
        totalPrice = findViewById(R.id.totalPrice);
        btnBetween = findViewById(R.id.btnBetween);
        btnNext = findViewById(R.id.btnNext);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("FIELD_ID")) {
            fieldId = intent.getIntExtra("FIELD_ID", 0);
            loadFieldInfoById(fieldId);
        } else {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin sân", Toast.LENGTH_SHORT).show();
            finish();
        }

        bookingDate = new Date();

        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDateText.setText("Ngày: " + displayDateFormat.format(bookingDate));
        selectedDateText.setOnClickListener(v -> showDatePickerDialog(this));

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TimeSlotAdapter(slot -> {
            if (slot.getStatus() == TimeSlot.AVAILABLE) {
                slot.toggleSelected();
                adapter.notifyDataSetChanged();
                updateTotalPrice();
            }
        });
        recyclerView.setAdapter(adapter);

        ApiTimeSlotInterface api = ApiClient.getClient().create(ApiTimeSlotInterface.class);
        apiTimeSlotService = new ApiTimeSlotService(api);

        dailySlots = generateDailyTimeSlots(bookingDate, fieldId);
        fetchBookedTimeSlots();

        btnNext.setOnClickListener(v -> {
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

            long currentTotalCost = selectedSlots.size() * 100000;

            Intent intentConfirm = new Intent(BookingActivity.this, ConfirmBookingActivity.class);
            intentConfirm.putExtra("FIELD_ID", fieldId);
            intentConfirm.putExtra("BOOKING_DATE", bookingDate.getTime());
            intentConfirm.putParcelableArrayListExtra("SELECTED_SLOTS", (ArrayList<TimeSlot>) selectedSlots);
            intentConfirm.putExtra("TOTAL_COST", currentTotalCost);
            startActivity(intentConfirm);
        });
    }

    private void loadFieldInfoById(int id) {
        ApiFieldInterface apiFieldInterface = ApiClient.getClient().create(ApiFieldInterface.class);
        ApiFieldService apiFieldService = new ApiFieldService(apiFieldInterface);

        apiFieldService.getFieldById(id, new ApiFieldService.ApiCallback<Field>() {
            @Override
            public void onSuccess(Field field) {
                fieldNameText.setText(field.getName());
                fieldAddressText.setText(field.getLocation());
                fieldNumberText.setText(String.valueOf(field.getCapacity()));

                if (field.getImages() != null && !field.getImages().isEmpty()) {
                    Glide.with(BookingActivity.this)
                            .load(field.getImages())
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(fieldImage);
                } else {
                    fieldImage.setImageResource(R.drawable.field2);
                }
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(BookingActivity.this, "Lỗi khi tải thông tin sân", Toast.LENGTH_SHORT).show();
                Log.e("BookingActivity", "Lỗi API: " + t.getMessage());
            }
        });
    }

    private List<TimeSlot> generateDailyTimeSlots(Date bookingDate, int fieldId) {
        List<TimeSlot> slots = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bookingDate);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        int slotIdCounter = 0;
        for (int i = 0; i < 24; i++) {
            Date start = calendar.getTime();
            String startTime = timeFormat.format(start);

            calendar.add(Calendar.MINUTE, 60);
            Date end = calendar.getTime();
            String endTime = timeFormat.format(end);

            TimeSlot slot = new TimeSlot(slotIdCounter++, fieldId, bookingDate, startTime, endTime, TimeSlot.AVAILABLE);
            slots.add(slot);
        }
        return slots;
    }

    private void fetchBookedTimeSlots() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateStr = dateFormat.format(bookingDate);

        apiTimeSlotService.getTimeSlots(fieldId, dateStr, new ApiTimeSlotService.ApiCallback<TimeslotResponseBooking>() {
            @Override
            public void onSuccess(TimeslotResponseBooking bookedSlots) {
                mergeBookedSlots(bookedSlots);
                runOnUiThread(() -> adapter.submitList(new ArrayList<>(dailySlots)));
            }

            @Override
            public void onError(Throwable t) {
                runOnUiThread(() -> adapter.submitList(new ArrayList<>(dailySlots)));
            }
        });
    }

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

    private boolean isOverlap(Date start, Date end, Date bStart, Date bEnd) {
        return start.before(bEnd) && bStart.before(end);
    }

    private void updateTotalPrice() {
        int count = 0;
        for (TimeSlot slot : dailySlots) {
            if (slot.isSelected() && slot.getStatus() == TimeSlot.AVAILABLE) {
                count++;
            }
        }
        long total = count * 100000;
        totalPrice.setText("Tổng tiền: " + total + " VND");
    }

    private void showDatePickerDialog(Context context) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, y, m, d) -> {
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(y, m, d);
            bookingDate = selectedDate.getTime();
            updateBookingDateUI();
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateBookingDateUI() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateString = sdf.format(bookingDate);
        selectedDateText.setText("Ngày: " + dateString);
        fetchBookedTimeSlots();
    }
}
