package com.example.btl.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.activities.FieldDetailActivity;
import com.example.btl.models.Field;
import java.util.List;

public class FieldAdapter extends RecyclerView.Adapter<FieldAdapter.ViewHolder> {

    private Context context;
    private List<Field> fieldList;

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
        holder.fieldAddress.setText(field.getAddress());
        holder.fieldNumber.setText("Số điện thoại: " + field.getNumber());
        holder.fieldImage.setImageResource(field.getImage());

        // Xử lý sự kiện click vào sân bóng
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FieldDetailActivity.class);
            intent.putExtra("name", field.getName());
            intent.putExtra("address", field.getAddress());
            intent.putExtra("number", field.getNumber());
            intent.putExtra("image", field.getImage());
            context.startActivity(intent);
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
