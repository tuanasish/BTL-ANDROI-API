package com.example.btl.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl.R;
import com.example.btl.activities.FieldDetailActivity;
import com.example.btl.models.Field;

import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {

    private Context context;
    private List<Field> fieldList;
    private OnItemClickListener onItemClickListener;  // Khai báo listener

    public FieldAdapter(Context context, List<Field> fieldList, OnItemClickListener listener) {
        this.context = context;
        this.fieldList = fieldList;
        this.onItemClickListener = listener;
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
        holder.fieldNumber.setText(String.valueOf(field.getCapacity()));

        // Load image using Glide
        if (field.getImages() != null && !field.getImages().isEmpty()) {
            Glide.with(context)
                    .load(field.getImages())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.fieldImage);
        } else {
            holder.fieldImage.setImageResource(R.drawable.field2);
        }

        // Gọi listener khi nhấn vào item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(field);  // Truyền đối tượng Field vào listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fieldName, fieldAddress, fieldNumber;
        ImageView fieldImage;

        public ViewHolder(View itemView) {
            super(itemView);
            fieldName = itemView.findViewById(R.id.fieldName);
            fieldAddress = itemView.findViewById(R.id.fieldAddress);
            fieldNumber = itemView.findViewById(R.id.fieldNumber);
            fieldImage = itemView.findViewById(R.id.fieldImage);
        }
    }

    // Định nghĩa interface
    public interface OnItemClickListener {
        void onItemClick(Field field);
    }
}
