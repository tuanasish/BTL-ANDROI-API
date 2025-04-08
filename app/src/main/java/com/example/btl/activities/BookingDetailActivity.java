package com.example.btl.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;
import com.example.btl.models.TimeSlot;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView txtDetailFieldName, txtDetailBookedDate, txtDetailTimeSlot, txtDetailTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        txtDetailFieldName = findViewById(R.id.txtDetailFieldName);
        txtDetailBookedDate = findViewById(R.id.txtDetailBookedDate);
        txtDetailTimeSlot = findViewById(R.id.txtDetailTimeSlot);
        txtDetailTotalPrice = findViewById(R.id.txtDetailTotalPrice);

        // Nhận dữ liệu từ intent
        TimeSlot slot = getIntent().getParcelableExtra("booking_detail");

        if (slot != null) {
            txtDetailFieldName.setText("Sân: " + slot.getFieldID());
            txtDetailBookedDate.setText("Ngày đặt: " + slot.getBookingDate());
            txtDetailTimeSlot.setText("Khung giờ: " + slot.getTimeRange());
//            txtDetailTotalPrice.setText("Tổng tiền: " + slot.() + " VND");
        }
    }
}
