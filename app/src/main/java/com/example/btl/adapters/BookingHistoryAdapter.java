package com.example.btl.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.models.TimeSlot;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<TimeSlot> bookings;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TimeSlot slot);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BookingHistoryAdapter(Context context, List<TimeSlot> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFieldName, txtFieldAddress, txtFieldNumber, txtBookedDate, txtTimeSlot, txtTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFieldName = itemView.findViewById(R.id.txtFieldName);
            txtFieldAddress = itemView.findViewById(R.id.txtFieldAddress);
            txtFieldNumber = itemView.findViewById(R.id.txtFieldNumber);
            txtBookedDate = itemView.findViewById(R.id.txtBookedDate);
            txtTimeSlot = itemView.findViewById(R.id.txtTimeSlot);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
        }
    }
}
