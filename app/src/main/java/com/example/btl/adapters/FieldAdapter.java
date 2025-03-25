package com.example.btl.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl.R;
import com.example.btl.activities.FieldDetailActivity;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiFieldService;
import com.example.btl.models.Field;
import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {

    private Context context;
    private List<Field> fieldList;
    private ApiFieldService api;

    public FieldAdapter(Context context, List<Field> fieldList) {
        this.context = context;
        this.fieldList = fieldList;
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


        // Load image
        if (field.getImages() != null && !field.getImages().isEmpty()) {
            // Sử dụng thư viện như Glide từ URL
            Glide.with(context)
                    .load(field.getImages())
                    .placeholder(R.drawable.ic_launcher_background) // Ảnh placeholder nếu load lỗi
                    .into(holder.fieldImage);
        } else {
            holder.fieldImage.setImageResource(R.drawable.field2); // Ảnh mặc định
        }

        //su kien click item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi API để lấy dữ liệu chi tiết của Field
                api.getFieldById(field.getField_id(), new ApiFieldService.ApiCallback<Field>() {
                    @Override
                    public void onSuccess(Field result) {
                        Log.d("DEBUG", "Received Field data: " + result);
                        // Tạo Intent và truyền đối tượng Field
                        Intent intent = new Intent(context, FieldDetailActivity.class);
                        intent.putExtra("FIELD_DATA", result);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(context, "Lỗi tải dữ liệu sân", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

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
}
