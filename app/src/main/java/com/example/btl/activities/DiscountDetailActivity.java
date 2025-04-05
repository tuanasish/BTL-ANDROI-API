package com.example.btl.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.btl.R;

public class DiscountDetailActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, expiryTextView;
    private ImageView discountImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount_detail);

        discountImageView = findViewById(R.id.discountImageDetail);

        int imageResId = getIntent().getIntExtra("imageResId", R.drawable.field1);

        discountImageView.setImageResource(imageResId);
    }
}
