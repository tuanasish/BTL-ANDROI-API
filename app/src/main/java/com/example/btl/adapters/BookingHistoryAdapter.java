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
    public void onBindViewHolder(@NonNull BookingHistoryAdapter.ViewHolder holder, int position) {
        TimeSlot slot = bookings.get(position);

        holder.txtFieldName.setText("Sân: " + slot.getFieldName());
        holder.txtFieldAddress.setText("Địa chỉ: " + slot.getFieldAddress());
        holder.txtFieldNumber.setText("SĐT: " + slot.getFieldNumber());
        holder.txtBookedDate.setText("Ngày đặt: " + slot.getBookedDate());
        holder.txtTimeSlot.setText("Khung giờ: " + slot.getTime());
        holder.txtTotalPrice.setText("Tổng tiền: " + slot.getTotalPrice() + " VND");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(slot);
            }
        });
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
