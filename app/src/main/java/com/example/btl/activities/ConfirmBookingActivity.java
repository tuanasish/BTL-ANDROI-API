package com.example.btl.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
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
    private static final String CHANNEL_ID = "my_channel_id"; // Khai báo CHANNEL_ID
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;  // Request code for permission
    private static final int NOTIFICATION_ID_BASE = 1000; // Base ID for notification to differentiate between different slots
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Tạo Notification Channel nếu cần (đảm bảo chỉ làm điều này một lần)
        createNotificationChannel();

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

        // Kiểm tra quyền POST_NOTIFICATIONS trên Android 13 hoặc cao hơn
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            } else {
                createNotificationChannel();
            }
        } else {
            createNotificationChannel(); // Nếu Android phiên bản thấp hơn 13, tạo channel mà không cần yêu cầu quyền
        }

        Intent intent = getIntent();
        /*selectedSlots = intent.getParcelableArrayListExtra("selected_slots");*/
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
            /*groupedSlots.putIfAbsent(slot.getFieldName(), new ArrayList<>());
            groupedSlots.get(slot.getFieldName()).add(slot.getTime());*/
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
                /*slot.setFieldAddress(fieldAddress);
                slot.setFieldNumber(fieldNumber);
                databaseHelper.addBooking(slot, bookedDate, totalCost);
*/
            //    showNotification(slot.getFieldName(), "Đặt sân " + slot.getFieldName() + " thành công!");
            }
            showNotification("Đặt sân thành công", "Bạn đã đặt sân thành công!");

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
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notifications";
            String description = "Channel for my app notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }
    }

    // Phương thức để hiển thị thông báo cho từng sân riêng biệt
//    private void showNotification(String fieldName, String message) {
//        // Tạo Notification cho mỗi sân
//        int notificationId = NOTIFICATION_ID_BASE + fieldName.hashCode(); // Tạo ID thông báo riêng biệt cho từng sân
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.dadat) // Đảm bảo icon hợp lệ
//                .setContentTitle("Thông báo mới" )
//                .setContentText(message)  // Cập nhật nội dung thông báo
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setOngoing(true) // Đảm bảo thông báo không tự động bị tắt
//                .setAutoCancel(false); // Giữ thông báo trên thanh trạng thái
//
//        notificationManager.notify(notificationId, builder.build()); // Dùng ID duy nhất cho mỗi sân
//    }
    // Phương thức để hiển thị thông báo chung
    private void showNotification(String title, String message) {
        // Tạo Notification chung cho tất cả các sân
        int notificationId = NOTIFICATION_ID_BASE; // Sử dụng ID duy nhất cho thông báo chung

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.dadat) // Đảm bảo icon hợp lệ
                .setContentTitle(title)          // Tiêu đề thông báo
                .setContentText(message)         // Nội dung thông báo
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(false)               // Không cho phép thông báo này kéo dài
                .setAutoCancel(true);            // Cho phép tự động tắt thông báo khi người dùng nhấn

        notificationManager.notify(notificationId, builder.build()); // Dùng ID chung cho thông báo
    }


}