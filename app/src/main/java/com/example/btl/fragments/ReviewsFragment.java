package com.example.btl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.btl.R;

public class ReviewsFragment extends Fragment {

    private EditText etYourReview;
    private Button btnSubmitReview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);

        // Initialize views
        etYourReview = rootView.findViewById(R.id.etYourReview);
        btnSubmitReview = rootView.findViewById(R.id.btnSubmitReview);

        // Set a click listener on the submit button
        btnSubmitReview.setOnClickListener(v -> {
            String userReview = etYourReview.getText().toString().trim();

            if (userReview.isEmpty()) {
                // Show a message if the review is empty
                Toast.makeText(getActivity(), "Vui lòng nhập đánh giá của bạn", Toast.LENGTH_SHORT).show();
            } else {
                // Handle the review submission (e.g., show a confirmation or save the review)
                Toast.makeText(getActivity(), "Cảm ơn bạn đã gửi đánh giá!", Toast.LENGTH_SHORT).show();

                // Optionally clear the input field after submission
                etYourReview.setText("");
            }
        });

        return rootView;
    }
}
