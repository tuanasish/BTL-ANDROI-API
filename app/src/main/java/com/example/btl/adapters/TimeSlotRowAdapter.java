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
    private final List<TimeSlot> timeSlots;
    private final OnTimeSlotClickListener listener;

    public interface OnTimeSlotClickListener {
        void onTimeSlotClicked(TimeSlot timeSlot, boolean isSelected);
    }

    public TimeSlotRowAdapter(List<TimeSlot> timeSlots, OnTimeSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot timeSlot = timeSlots.get(position);

        // Nếu đã đặt → khóa tương tác + làm mờ + hiển thị (Đã đặt)
        if (timeSlot.getStatus() == TimeSlot.LOCKED) {
            holder.timeText.setText(timeSlot.getTime() + " (Đã đặt)");
            holder.itemView.setAlpha(0.4f); // Làm mờ
            holder.itemView.setOnClickListener(null); // Không cho bấm
            holder.itemView.setBackgroundResource(R.color.locked_color); // Màu riêng cho đã đặt
        } else {
            // Nếu chưa đặt → xử lý bình thường
            holder.timeText.setText(timeSlot.getTime());
            holder.itemView.setAlpha(1.0f);
            holder.itemView.setOnClickListener(v -> {
                timeSlot.toggleSelected(); // Chọn/huỷ chọn
                listener.onTimeSlotClicked(timeSlot, timeSlot.isSelected());
                notifyDataSetChanged();
            });

            // Thay đổi màu khi được chọn
            if (timeSlot.isSelected()) {
                holder.itemView.setBackgroundResource(R.color.selected_color);
            } else {
                holder.itemView.setBackgroundResource(R.color.default_color);
            }
        }
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeText;

        public ViewHolder(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.timeText);
        }
    }
}
