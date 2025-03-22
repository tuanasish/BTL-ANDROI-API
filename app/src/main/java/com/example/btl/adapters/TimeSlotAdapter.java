package com.example.btl.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.models.TimeSlot;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {
    private final List<List<TimeSlot>> timeSlots;
    private final TimeSlotRowAdapter.OnTimeSlotClickListener listener;

    public TimeSlotAdapter(List<List<TimeSlot>> timeSlots, TimeSlotRowAdapter.OnTimeSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<TimeSlot> slotRow = timeSlots.get(position);
        holder.sportFieldName.setText("Pickleball " + (position + 1));
        TimeSlotRowAdapter rowAdapter = new TimeSlotRowAdapter(slotRow, listener);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(rowAdapter);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sportFieldName;
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            sportFieldName = itemView.findViewById(R.id.fieldName);
            recyclerView = itemView.findViewById(R.id.recyclerViewTimeSlots);
        }
    }
}
