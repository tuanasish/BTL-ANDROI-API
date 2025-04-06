package com.example.btl.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.models.TimeSlot;
import java.util.List;

public class TimeSlotRowAdapter extends RecyclerView.Adapter<TimeSlotRowAdapter.ViewHolder> {

    private final List<TimeSlot> timeSlots;  // Danh sách các khung giờ của một sân
    private final OnTimeSlotClickListener listener;

    // Constructor nhận vào danh sách các khung giờ và listener để xử lý sự kiện click
    public TimeSlotRowAdapter(List<TimeSlot> timeSlots, OnTimeSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    public TimeSlotRowAdapter(List<TimeSlot> slotRow, TimeSlotAdapter.OnTimeSlotClickListener listener, List<TimeSlot> timeSlots, OnTimeSlotClickListener listener1) {
        this.timeSlots = timeSlots;
        this.listener = listener1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi khung giờ
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot timeSlot = timeSlots.get(position);
        // Hiển thị thời gian bắt đầu và kết thúc của khung giờ
        holder.timeTextView.setText(timeSlot.getStart_time().toString() + " - " + timeSlot.getEnd_time().toString());

        // Set màu cho khung giờ đã đặt (màu đỏ) hoặc chưa đặt (màu xanh)
        if ("booked".equals(timeSlot.getStatus())) {
            holder.itemView.setBackgroundColor(0xFFFF0000); // Màu đỏ cho đã đặt
        } else {
            holder.itemView.setBackgroundColor(0xFF00FF00); // Màu xanh cho chưa đặt
        }

        // Xử lý sự kiện click vào khung giờ
        holder.itemView.setOnClickListener(v -> listener.onTimeSlotClick(timeSlot));
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();  // Trả về số lượng khung giờ
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView;  // TextView hiển thị thời gian của khung giờ

        public ViewHolder(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.timeSlotText);  // Ánh xạ TextView
        }
    }

    // Interface lắng nghe sự kiện click vào một khung giờ
    public interface OnTimeSlotClickListener {
        void onTimeSlotClick(TimeSlot timeSlot);  // Khi người dùng click vào khung giờ
    }
}
