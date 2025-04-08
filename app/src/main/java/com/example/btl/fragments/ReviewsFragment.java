package com.example.btl.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.adapters.ReviewAdapter;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiReviewInterface;
import com.example.btl.models.Review;
import com.example.btl.models.User;
import com.google.gson.Gson;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsFragment extends Fragment {

    private Button btnSubmitReview;
    private RecyclerView recyclerReviews;
    private List<Review> reviewList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private int fieldId;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

        btnSubmitReview = rootView.findViewById(R.id.btnSubmitReview);
        recyclerReviews = rootView.findViewById(R.id.recyclerReviews);

        reviewAdapter = new ReviewAdapter(reviewList);
        recyclerReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReviews.setAdapter(reviewAdapter);

        // Lấy FIELD_ID từ Intent
        fieldId = getActivity().getIntent().getIntExtra("FIELD_ID", -1);

        // Lấy USER_ID từ SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("USER_PREF", getContext().MODE_PRIVATE);
        String userJson = prefs.getString("USER_DATA", null);
        if (userJson != null) {
            User user = new Gson().fromJson(userJson, User.class);
            userId = user.getUser_id();
        } else {
            userId = -1;
        }

        loadReviews(fieldId);
        btnSubmitReview.setOnClickListener(v -> showReviewDialog());

        return rootView;
    }

    private void showReviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_submit_review, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        EditText etDialogReview = dialogView.findViewById(R.id.etDialogReview);
        Button btnDialogSubmit = dialogView.findViewById(R.id.btnDialogSubmit);

        btnDialogSubmit.setOnClickListener(submitView -> {
            String comment = etDialogReview.getText().toString().trim();
            if (comment.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }

            Review review = new Review(userId, fieldId, 5, comment, getCurrentDateTime());

            ApiReviewInterface api = ApiClient.getClient().create(ApiReviewInterface.class);
            api.createReview(review).enqueue(new Callback<Review>() {
                @Override
                public void onResponse(Call<Review> call, Response<Review> response) {
                    if (response.isSuccessful()) {
                        loadReviews(fieldId);
                        Toast.makeText(getContext(), "Gửi đánh giá thành công!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), "Gửi thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Review> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    private void loadReviews(int fieldId) {
        ApiReviewInterface api = ApiClient.getClient().create(ApiReviewInterface.class);
        api.getReviewsByFieldId(fieldId).enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList.clear();
                    reviewList.addAll(response.body());
                    reviewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi khi tải đánh giá", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
        return sdf.format(new Date());
    }
}
