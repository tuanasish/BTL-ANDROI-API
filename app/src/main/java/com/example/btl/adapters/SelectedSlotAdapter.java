package com.example.btl.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.models.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class SelectedSlotAdapter extends RecyclerView.Adapter<SelectedSlotAdapter.ViewHolder> {

    private List<TimeSlot> selectedSlots = new ArrayList<>();

    public SelectedSlotAdapter() {
        // Không cần truyền dữ liệu khi khởi tạo
    }

    public void setTimeSlotList(List<TimeSlot> slots) {
        this.selectedSlots = slots;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot slot = selectedSlots.get(position);
        holder.txtFieldName.setText("Sân ID: " + slot.getFieldID()); // chuyển int sang String
        holder.txtTime.setText("Khung giờ: " + slot.getTimeRange());
    }

    @Override
    public int getItemCount() {
        return selectedSlots != null ? selectedSlots.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFieldName, txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFieldName = itemView.findViewById(R.id.txtFieldName);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
