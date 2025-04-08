package com.example.btl.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl.R;
import com.example.btl.activities.FieldDetailActivity;
import com.example.btl.models.Field;

import java.util.ArrayList;
import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {

    private final List<Field> fieldList;
    private Context context;

    public FieldAdapter(Context context, List<Field> fieldList) {
        this.context = context;
        this.fieldList = (fieldList != null) ? fieldList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Field field = fieldList.get(position);
        holder.fieldName.setText(field.getName());
        holder.fieldAddress.setText(field.getLocation());
        holder.fieldNumber.setText("Sức chứa: " + field.getCapacity());
        holder.fieldPrice.setText("Giá: " + String.format("%,.0f", field.getPrice()) + " VND");

        // Load ảnh sân
        if (field.getImages() != null && !field.getImages().isEmpty()) {
            Glide.with(context)
                    .load(field.getImages())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.fieldImage);
        } else {
            holder.fieldImage.setImageResource(R.drawable.field2);
        }

        // Tạo Intent mở FieldDetailActivity
        View.OnClickListener openDetail = v -> {
            Log.d("FieldAdapter", "Mở chi tiết sân: ID = " + field.getField_id());
            Intent intent = new Intent(context, FieldDetailActivity.class);
            intent.putExtra("FIELD_ID", field.getField_id());
            context.startActivity(intent);
        };

        // Click toàn bộ item
        holder.itemView.setOnClickListener(openDetail);

        // Click nút Đặt lịch
        holder.btnBookField.setOnClickListener(openDetail);
    }

    @Override
    public int getItemCount() {
        return (fieldList != null) ? fieldList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fieldName, fieldAddress, fieldNumber, fieldPrice;
        ImageView fieldImage;
        Button btnBookField;

        public ViewHolder(View itemView) {
            super(itemView);
            fieldName = itemView.findViewById(R.id.fieldName);
            fieldAddress = itemView.findViewById(R.id.fieldAddress);
            fieldNumber = itemView.findViewById(R.id.fieldNumber);
            fieldPrice = itemView.findViewById(R.id.fieldPrice);
            fieldImage = itemView.findViewById(R.id.fieldImage);
            btnBookField = itemView.findViewById(R.id.btnBookField);
        }
    }
}
