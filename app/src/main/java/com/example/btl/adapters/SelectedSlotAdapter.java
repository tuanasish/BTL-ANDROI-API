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

public class SelectedSlotAdapter extends RecyclerView.Adapter<SelectedSlotAdapter.ViewHolder> {

    private final List<TimeSlot> selectedSlots;

    public SelectedSlotAdapter(List<TimeSlot> selectedSlots) {
        this.selectedSlots = selectedSlots;
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

    }

    @Override
    public int getItemCount() {
        return selectedSlots.size();
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
