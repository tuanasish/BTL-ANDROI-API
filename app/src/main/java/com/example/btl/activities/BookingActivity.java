package com.example.btl.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.adapters.TimeSlotAdapter;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiTimeSlotInterface;
import com.example.btl.api.ApiTimeSlotService;
import com.example.btl.models.CourtResponse;
import com.example.btl.models.TimeSlot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BookingActivity extends AppCompatActivity {
    private int index = 1; // FIELD_ID
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
    private ApiTimeSlotService api;

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
        selectedDateText = findViewById(R.id.selectedDateText);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            fieldName = intent.getStringExtra("name");
            fieldAddress = intent.getStringExtra("address");
            fieldNumber = intent.getStringExtra("number");
            fieldImageRes = intent.getIntExtra("image", R.drawable.ic_launcher_background);
            index = intent.getIntExtra("FIELD_ID", 1);
            fieldNameText.setText(fieldName);
            fieldAddressText.setText(fieldAddress);
            fieldNumberText.setText("Số điện thoại: " + fieldNumber);
            fieldImage.setImageResource(fieldImageRes);
        }

        // Khởi tạo API service cho TimeSlot
        ApiTimeSlotInterface apiTimeSlotInterface = ApiClient.getClient().create(ApiTimeSlotInterface.class);
        api = new ApiTimeSlotService(apiTimeSlotInterface);

        // Bấm chọn ngày: mở DatePickerDialog
        selectedDateText.setOnClickListener(v -> showDatePickerDialog());

        // Bấm nút tiếp tục
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
            confirmIntent.putExtra("FIELD_ID", index);

            if (!selectedSlots.isEmpty()) {
                TimeSlot firstSlot = selectedSlots.get(0);
                confirmIntent.putExtra("selected_time", firstSlot.getTime());
                confirmIntent.putExtra("selected_duration", selectedSlots.size() * 90);
            }
            startActivity(confirmIntent);
        });
    }

    /**
     * Callback update price khi người dùng chọn hoặc bỏ chọn một slot.
     */
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

    /**
     * Hiển thị DatePickerDialog để chọn ngày, sau đó gọi API lấy danh sách slot đã đặt của ngày đó.
     */
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    // Định dạng ngày theo dd/MM/yyyy
                    selectedDate = dayOfMonth + "-" + (month1 + 1) + "-" + year1;
                    selectedDateText.setText("Ngày chọn: " + selectedDate);

                    // Reset lại các slot được chọn và tổng tiền
                    selectedSlots.clear();
                    totalCost = 0;
                    totalPrice.setText("Tổng tiền: 0 VND");

                    // Gọi API để lấy danh sách các khung giờ đã được đặt theo FIELD_ID và ngày
                    api.getTimeSlots(index, selectedDate, new ApiTimeSlotService.ApiCallback<List<TimeSlot>>() {
                        @Override
                        public void onSuccess(List<TimeSlot> bookedSlots) {
                            // Tạo lại danh sách các khung giờ dựa vào dữ liệu trả về từ API
                            timeSlotList = generateTimeSlots(index, selectedDate, bookedSlots);
                            // Khởi tạo adapter và gán vào RecyclerView
                            adapter = new TimeSlotAdapter(timeSlotList, BookingActivity.this::updatePrice);
                            recyclerView.setAdapter(adapter);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(BookingActivity.this, "Lỗi khi tải dữ liệu khung giờ!", Toast.LENGTH_SHORT).show();
                        }
                    });
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    /**
     * Tạo danh sách slot dựa trên ngày đã chọn và danh sách slot đã được đặt (bookedSlots) từ API.
     * Nếu một slot có thời gian trùng khớp với một trong các slot đã được đặt thì sẽ đánh dấu là LOCKED.
     */
    private List<List<TimeSlot>> generateTimeSlots(final int fieldId, String selectedDate, final List<TimeSlot> bookedSlots) {
        // Sử dụng mảng kích thước 1 để "giữ" giá trị từ callback (do biến phải final hoặc hiệu ứng final)
        final int[] count = {0};
        final List<Integer>[] courtIds = new List[]{new ArrayList<>()};

        // CountDownLatch để đợi kết quả từ API bất đồng bộ
        final CountDownLatch latch = new CountDownLatch(1);

        // Gọi API lấy thông tin số lượng sân và danh sách id
        api.getCountCourt(fieldId, new ApiTimeSlotService.ApiCallback<CourtResponse>() {
            @Override
            public void onSuccess(CourtResponse result) {
                count[0] = result.getCourtCount();
                courtIds[0] = result.getCourtIds();
                latch.countDown();
            }

            @Override
            public void onError(Throwable t) {
                // Xử lý lỗi nếu cần (ở đây ta chỉ countDown để thoát khỏi latch)
                latch.countDown();
            }
        });

        // Chờ đợi kết quả API
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ArrayList<>();
        }

        // Tạo lưới thời gian với số hàng bằng số lượng sân (court count)
        List<List<TimeSlot>> allSlots = new ArrayList<>();

        // Danh sách khung giờ mặc định cho mỗi sân
        String[] times = {"5:00", "6:30", "8:00", "9:30", "11:00", "12:30", "14:00", "15:30", "17:00", "18:30", "20:00", "21:30", "23:00"};

        // Với mỗi sân, tạo một danh sách TimeSlot cho các khung giờ
        // Nếu API không trả về count hợp lệ (ví dụ 0), bạn có thể đặt một giá trị mặc định hoặc thông báo lỗi
        int numberOfCourts = count[0] > 0 ? count[0] : 1;
        for (int i = 0; i < numberOfCourts; i++) {
            List<TimeSlot> slotRow = new ArrayList<>();
            for (String time : times) {
                // Giả sử constructor của TimeSlot có dạng:
                // TimeSlot(String courtName, String time, String status, int duration, String bookingDate)
                TimeSlot slot = new TimeSlot("PickelBall " + (i + 1), time, TimeSlot.AVAILABLE, 90, selectedDate);

                // Kiểm tra nếu trong danh sách bookedSlots có khung giờ trùng với time hiện tại
                for (TimeSlot booked : bookedSlots) {
                    // Giả sử booked.getTime() trả về thời gian dạng chuỗi
                    if (booked.getTime().equals(time)) {
                        slot.setStatus(TimeSlot.LOCKED);
                        break;
                    }
                }
                slotRow.add(slot);
            }
            allSlots.add(slotRow);
        }

        return allSlots;
    }
}
