package com.example.btl.adapters;

import android.util.Log;
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

public class BookingHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;

    private List<TimeSlot> bookedSlots;
    private String bookedDate;
    private int totalCost;

    public BookingHistoryAdapter() {
        this.bookedSlots = new ArrayList<>();
        this.bookedDate = "";
        this.totalCost = 0;
    }

    public void updateList(List<TimeSlot> newList, String newDate, int newTotal) {
        Log.d("BookingHistoryAdapter", "🔹 updateList() called");
        Log.d("BookingHistoryAdapter", "📅 newDate: " + newDate + ", 💰 newTotal: " + newTotal);
        Log.d("BookingHistoryAdapter", "🕑 Slot count: " + (newList != null ? newList.size() : 0));

        if (newList == null) return; // Tránh lỗi null
        bookedSlots.clear();
        bookedSlots.addAll(newList);
        bookedDate = newDate;
        totalCost = newTotal;

        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_HEADER : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_header, parent, false);
            return new BookingHeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_history, parent, false);
            return new BookingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BookingHeaderViewHolder) {
            BookingHeaderViewHolder headerHolder = (BookingHeaderViewHolder) holder;

            if (bookedDate == null || bookedDate.isEmpty()) {
                headerHolder.txtBookedDate.setText("Ngày đặt: Không có dữ liệu");
            } else {
                headerHolder.txtBookedDate.setText("Ngày đặt: " + bookedDate);
            }

            headerHolder.txtTotalPrice.setText("Tổng tiền: " + totalCost + " VND");

            Log.d("BookingHistoryAdapter", "📢 Header displayed - Date: " + bookedDate + ", Total: " + totalCost);
        } else if (holder instanceof BookingViewHolder) {
            BookingViewHolder itemHolder = (BookingViewHolder) holder;
            TimeSlot slot = bookedSlots.get(position - 1); // Header chiếm vị trí đầu tiên

            itemHolder.txtFieldName.setText("Sân: " + slot.getFieldName());
            itemHolder.txtTimeSlot.setText("Khung giờ: " + slot.getTime());
        }
    }

    @Override
    public int getItemCount() {
        return bookedSlots.size() + 1; // Thêm 1 cho header
    }

    public static class BookingHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtBookedDate, txtTotalPrice;

        public BookingHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBookedDate = itemView.findViewById(R.id.txtBookedDateHeader);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPriceHeader);
        }
    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView txtFieldName, txtTimeSlot;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFieldName = itemView.findViewById(R.id.txtFieldName);
            txtTimeSlot = itemView.findViewById(R.id.txtTimeSlot);
        }
    }
}
