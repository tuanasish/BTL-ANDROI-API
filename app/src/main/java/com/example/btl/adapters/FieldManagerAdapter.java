package com.example.btl.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiFieldService;
import com.example.btl.models.Field;

import java.util.List;

import retrofit2.Call;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
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
            Toast.makeText(context, "Chỉnh sửa sân: " + field.getName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, EditFieldActivity.class);
            intent.putExtra("FIELD", field);
            ((android.app.Activity) context).startActivityForResult(intent, 1);
        });

        // Xóa sân
        holder.imgDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc muốn xóa sân " + field.getName() + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        ApiFieldService apiFieldService = new ApiFieldService(ApiClient.getClient().create(ApiFieldInterface.class));

                        apiFieldService.deleteField(field.getField_id(), new ApiFieldService.ApiCallback<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                fieldList.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context, "Đã xóa sân " + field.getName(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(context, "Lỗi khi xóa sân: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("DeleteField", "Lỗi: ", t);
                            }
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
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
