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

public class ServiceFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);

        // Tìm TextView và đặt danh sách dịch vụ
        TextView tvService = view.findViewById(R.id.tvService);
        tvService.setText(
                        "⚽ Thuê bóng\n" +
                        "👕 Thuê áo đội\n" +
                        "🚿 Phòng tắm nước nóng\n" +
                        "🥤 Đồ uống\n" +
                        "📸 Chụp ảnh đội bóng\n" +
                        "🎶 Âm thanh sân đấu\n" +
                        "💡 Hệ thống đèn ban đêm\n" +
                        "🚗 Bãi đỗ xe rộng"
        );

        return view;
    }
}
