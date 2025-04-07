package com.example.btl.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.models.BookingResponse;
import com.example.btl.models.TimeSlot;

import java.util.List;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<BookingResponse> bookings;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TimeSlot slot);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BookingHistoryAdapter(Context context, List<BookingResponse> bookings) {
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
        BookingResponse booking = bookings.get(position);

        holder.txtFieldName.setText("Tên sân: " + booking.getField_name());
        holder.txtBookedDate.setText("Ngày đặt: " + booking.getDate().substring(0, 10));
        holder.txtTimeSlot.setText("Khung giờ: " + booking.getStart_time() + " - " + booking.getEnd_time());

        String status = booking.getStatus();
        if (status == null || status.isEmpty()) {
            status = "Chờ xác nhận";
        }
        holder.txtStatus.setText("Trạng thái: " + status);

        holder.txtTotalPrice.setText("Tổng tiền: " + ((int) booking.getTotal_price()) + " đ");
    }



    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFieldName, txtBookedDate, txtTimeSlot, txtStatus, txtTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFieldName = itemView.findViewById(R.id.txtFieldName);
            txtBookedDate = itemView.findViewById(R.id.txtBookedDate);
            txtTimeSlot = itemView.findViewById(R.id.txtTimeSlot);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtTotalPrice = itemView.findViewById(R.id.txtTotalPrice);
        }
    }

}
