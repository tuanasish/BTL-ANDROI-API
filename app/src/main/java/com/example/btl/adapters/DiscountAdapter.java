package com.example.btl.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.BookingActivity;
import com.example.btl.R;
import com.example.btl.activities.DiscountDetailActivity;
import com.example.btl.models.Discount;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder> {

    private Context context;
    private List<Discount> discountList;

    public DiscountAdapter(Context context, List<Discount> discountList) {
        this.context = context;
        this.discountList = discountList;
    }

    @Override
    public DiscountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_discount, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiscountViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        holder.image.setImageResource(discount.getImageResId());

        // Sự kiện click toàn bộ item -> mở DiscountDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DiscountDetailActivity.class);
            intent.putExtra("imageResId", discount.getImageResId());
            context.startActivity(intent);
        });

        // ✅ Sự kiện click "Đặt lịch ngay" -> mở BookingActivity với đầy đủ thông tin
        holder.scheduleButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingActivity.class);
            intent.putExtra("FIELD_ID", discount.getFieldId());  // ✅ Key chính xác
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        Button scheduleButton;

        public DiscountViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.discountImage);
            scheduleButton = itemView.findViewById(R.id.scheduleButton);
        }
    }
}
