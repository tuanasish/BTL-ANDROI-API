package com.example.btl.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.activities.EditUserActivity;
import com.example.btl.models.User;

import java.util.List;

public class UserManagerAdapter extends RecyclerView.Adapter<UserManagerAdapter.ViewHolder> {

    private Context context;
    private List<User> userList;

    public UserManagerAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = userList.get(position);

        // Kiểm tra nếu username không null hoặc rỗng
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            holder.userName.setText(user.getUsername());
        } else {
            holder.userName.setText("Tên người dùng không có sẵn");
        }

        // Kiểm tra nếu email không null hoặc rỗng
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            holder.userEmail.setText(user.getEmail());
        } else {
            holder.userEmail.setText("Email không có sẵn");
        }

        // Sự kiện chỉnh sửa người dùng
        holder.btnEdit.setOnClickListener(v -> {
            // Mở EditUserActivity để chỉnh sửa thông tin người dùng
            Intent intent = new Intent(context, EditUserActivity.class);
            intent.putExtra("USER", user);  // Truyền đối tượng User để chỉnh sửa
            context.startActivity(intent);
        });

        // Sự kiện xóa người dùng
        holder.btnDelete.setOnClickListener(v -> {
            // Xóa người dùng khỏi danh sách
            if (position >= 0 && position < userList.size()) {
                userList.remove(position);
                notifyItemRemoved(position);  // Cập nhật RecyclerView sau khi xóa
                Toast.makeText(context, "Người dùng đã xóa: " + user.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail;
        Button btnEdit, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
