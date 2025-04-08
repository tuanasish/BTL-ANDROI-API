package com.example.btl.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.models.Review;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvComment, tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.tvUserName.setText(review.getUser_name());
        holder.tvComment.setText(review.getComment());
        String formattedDate = formatDateTime(review.getReview_date());
        holder.tvDate.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
    private String formatDateTime(String isoDateTime) {
        try {
            // Format gốc từ backend: ISO 8601
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", java.util.Locale.getDefault());

            // Format hiển thị ra giao diện
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault());

            return outputFormat.format(inputFormat.parse(isoDateTime));
        } catch (Exception e) {
            e.printStackTrace();
            return isoDateTime; // fallback nếu lỗi
        }
    }

}
