package com.example.btl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.btl.R;

public class ImagesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        // Ánh xạ các ImageView và đặt ảnh sân bóng
        ImageView img1 = view.findViewById(R.id.img1);
        ImageView img2 = view.findViewById(R.id.img2);
        ImageView img3 = view.findViewById(R.id.img3);

        img1.setImageResource(R.drawable.field1);
        img2.setImageResource(R.drawable.field2);
        img3.setImageResource(R.drawable.field4);

        return view;
    }
}
