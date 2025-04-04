package com.example.btl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.btl.R;

public class DetailFieldFragment extends Fragment {

    private TextView tvFieldDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_field, container, false);

        tvFieldDetails = view.findViewById(R.id.tvFieldDetails);

        // Hiển thị thông tin chi tiết sân bóng
        String fieldDetails = "🏟 Sân có diện tích 1000m²\n" +
                "⚽ Bao gồm nhiều sân nhỏ\n" +
                "🌳 Không gian thoáng rộng\n" +
                "⏰ Mở cửa từ sáng đến tối";

        tvFieldDetails.setText(fieldDetails);

        return view;
    }
}
