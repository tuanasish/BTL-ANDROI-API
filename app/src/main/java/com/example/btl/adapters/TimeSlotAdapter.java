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

public class TimeSlotAdapter extends ListAdapter<TimeSlot, TimeSlotAdapter.ViewHolder> {

    public interface OnSlotClickListener {
        void onSlotClick(TimeSlot slot);
    }

    private final OnSlotClickListener listener;

    public TimeSlotAdapter(OnSlotClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot slot = getItem(position);

        // Set time range
        holder.tvTime.setText(slot.getTimeRange());

        // Update UI state
        updateSlotAppearance(holder, slot);

        // Handle interactions
        setupClickListeners(holder, slot);
    }

    private void updateSlotAppearance(ViewHolder holder, TimeSlot slot) {
        Context context = holder.itemView.getContext();
        int bgResId = R.drawable.bg_selected_time_slot;
        int textColor = ContextCompat.getColor(context, R.color.default_color);

        switch (slot.getStatus()) {
            case TimeSlot.AVAILABLE:
                bgResId = R.drawable.bg_available_time_slot;
                textColor = ContextCompat.getColor(context, R.color.black);
                break;
            case TimeSlot.BOOKED:
                bgResId = R.drawable.bg_selected_time_slot;
                textColor = ContextCompat.getColor(context, R.color.default_color);
                break;
            case TimeSlot.LOCKED:
                bgResId = R.drawable.bg_locked_time_slot;
                break;
        }

        // Áp dụng background, màu chữ và hiệu ứng alpha cho trạng thái chọn
        holder.itemView.setBackgroundResource(bgResId);
        holder.tvTime.setTextColor(textColor);
        holder.itemView.setAlpha(slot.isSelected() ? 1f : 0.5f);
    }


    private void setupClickListeners(ViewHolder holder, TimeSlot slot) {
        if (slot.getStatus() == TimeSlot.AVAILABLE || slot.getStatus() == TimeSlot.BOOKED) {
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onSlotClick(slot);
                    notifyItemChanged(holder.getBindingAdapterPosition());
                }
            });
        } else {
            holder.itemView.setOnClickListener(null);
            holder.itemView.setClickable(false);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTime;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}