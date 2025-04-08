package com.example.btl.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.models.TimeSlot;

public class TimeSlotRowAdapter extends ListAdapter<TimeSlot, TimeSlotRowAdapter.ViewHolder> {

    private final OnTimeSlotClickListener listener;
    private int lastSelectedPosition = -1;

    public interface OnTimeSlotClickListener {
        void onTimeSlotClicked(TimeSlot timeSlot);
    }

    // Sử dụng DiffUtil để tối ưu cập nhật
    private static final DiffUtil.ItemCallback<TimeSlot> DIFF_CALLBACK = new DiffUtil.ItemCallback<TimeSlot>() {
        @Override
        public boolean areItemsTheSame(@NonNull TimeSlot oldItem, @NonNull TimeSlot newItem) {
            return oldItem.getSlotID() == newItem.getSlotID();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull TimeSlot oldItem, @NonNull TimeSlot newItem) {
            return oldItem.equals(newItem);
        }
    };

    public TimeSlotRowAdapter(OnTimeSlotClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot timeSlot = getItem(position);
        Context context = holder.itemView.getContext();

        // Hiển thị thời gian
        holder.timeText.setText(timeSlot.getTimeRange());

        // Xử lý trạng thái
        if (timeSlot.getStatus() == TimeSlot.LOCKED || timeSlot.getStatus() == TimeSlot.BOOKED) {
            handleLockedState(holder, timeSlot, context);
        } else {
            handleAvailableState(holder, timeSlot, context);
        }
    }

    private void handleLockedState(ViewHolder holder, TimeSlot timeSlot, Context context) {
        String statusText = timeSlot.getStatus() == TimeSlot.BOOKED ? " (Đã đặt)" : " (Khóa)";
        holder.timeText.setText(timeSlot.getTimeRange() + statusText);

        // Thiết lập giao diện
        holder.itemView.setAlpha(0.5f);
        holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_locked_time_slot));
        holder.itemView.setOnClickListener(null);
    }

    private void handleAvailableState(ViewHolder holder, TimeSlot timeSlot, Context context) {
        // Thiết lập giao diện mặc định
        holder.itemView.setAlpha(1f);
        holder.itemView.setBackground(ContextCompat.getDrawable(context,
                timeSlot.isSelected() ? R.drawable.bg_selected_time_slot : R.drawable.bg_available_time_slot));

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            TimeSlot currentItem = getItem(currentPosition);

            // Xử lý chọn/bỏ chọn
            handleItemSelection(currentPosition, currentItem);
        });
    }

    private void handleItemSelection(int currentPosition, TimeSlot currentItem) {
        // Bỏ chọn item trước đó
        if (lastSelectedPosition != -1 && lastSelectedPosition != currentPosition) {
            TimeSlot previousItem = getItem(lastSelectedPosition);
            previousItem.setSelected(false);
            notifyItemChanged(lastSelectedPosition);
        }

        // Cập nhật trạng thái hiện tại
        boolean newState = !currentItem.isSelected();
        currentItem.setSelected(newState);
        lastSelectedPosition = newState ? currentPosition : -1;
        notifyItemChanged(currentPosition);

        // Thông báo sự kiện
        if (listener != null) {
            listener.onTimeSlotClicked(currentItem);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView timeText;

        ViewHolder(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.tvTime);
        }
    }
}