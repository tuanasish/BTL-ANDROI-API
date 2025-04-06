package com.example.btl.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.btl.R;
import com.example.btl.activities.EditFieldActivity;
import com.example.btl.models.Field;

import java.util.List;

public class FieldManagerAdapter extends RecyclerView.Adapter<FieldManagerAdapter.ViewHolder> {

    private Context context;
    private List<Field> fieldList;

    public FieldManagerAdapter(Context context, List<Field> fieldList) {
        this.context = context;
        this.fieldList = fieldList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_field_manage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Field field = fieldList.get(position);

        // Hiển thị thông tin sân
        holder.fieldName.setText(field.getName());
        holder.fieldLocation.setText(field.getLocation());
        holder.fieldCapacity.setText("Sức chứa: " + field.getCapacity());
        String imageUrl = field.getImages();
        Log.d("FieldAdapter", "Image URL: " + imageUrl);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.field2) // ảnh tạm trong lúc load
                    .into(holder.fieldImage);
        } else {
            holder.fieldImage.setImageResource(R.drawable.field2);
        }

        // Chỉnh sửa sân
        holder.imgEdit.setOnClickListener(v -> {
            // Mở màn hình chỉnh sửa sân
            Toast.makeText(context, "Chỉnh sửa sân: " + field.getName(), Toast.LENGTH_SHORT).show();

            // Mở EditFieldActivity để chỉnh sửa thông tin sân
            Intent intent = new Intent(context, EditFieldActivity.class);
            intent.putExtra("FIELD", field);  // Truyền đối tượng Field để chỉnh sửa
            ((android.app.Activity) context).startActivityForResult(intent, 1);  // Yêu cầu trả kết quả về sau khi chỉnh sửa
        });

        // Xóa sân
        holder.imgDelete.setOnClickListener(v -> {
            // Xóa sân khỏi danh sách
            fieldList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Sân đã xóa: " + field.getName(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fieldName, fieldLocation, fieldCapacity;
        ImageView fieldImage, imgEdit, imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            fieldName = itemView.findViewById(R.id.fieldName);
            fieldLocation = itemView.findViewById(R.id.fieldLocation);
            fieldCapacity = itemView.findViewById(R.id.fieldCapacity);
            fieldImage = itemView.findViewById(R.id.fieldImage);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
}
