package com.example.btl.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.btl.BookingActivity;
import com.example.btl.R;

import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiFieldService;
import com.example.btl.models.Field;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FieldDetailBottomSheet extends BottomSheetDialogFragment {

    private static final String ARG_FIELD_ID = "field_id";
    private static final String ARG_LATITUDE = "lat";
    private static final String ARG_LONGITUDE = "lng";

    private int fieldId;
    private double latitude, longitude;

    private ImageView imgField;
    private TextView txtName, txtAddress, txtPhone;
    private TextView txtType, txtPrice, txtDescription;
    private Button btnBooking, btnDirection;

    public static FieldDetailBottomSheet newInstance(int fieldId, double latitude, double longitude) {
        FieldDetailBottomSheet fragment = new FieldDetailBottomSheet();
        Bundle args = new Bundle();
        args.putInt(ARG_FIELD_ID, fieldId);
        args.putDouble(ARG_LATITUDE, latitude);
        args.putDouble(ARG_LONGITUDE, longitude);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_field_detail, container, false);

        imgField = view.findViewById(R.id.imgField);
        txtName = view.findViewById(R.id.txtFieldName);
        txtAddress = view.findViewById(R.id.txtFieldAddress);
        txtPhone = view.findViewById(R.id.txtFieldPhone);
        txtType = view.findViewById(R.id.txtType);
        txtPrice = view.findViewById(R.id.txtPrice);
        txtDescription = view.findViewById(R.id.txtDescription);
        btnBooking = view.findViewById(R.id.btnBooking);
        btnDirection = view.findViewById(R.id.btnDirection);

        if (getArguments() != null) {
            fieldId = getArguments().getInt(ARG_FIELD_ID, -1);
            latitude = getArguments().getDouble(ARG_LATITUDE, 0.0);
            longitude = getArguments().getDouble(ARG_LONGITUDE, 0.0);
        }

        fetchFieldDetail();

        return view;
    }

    private void fetchFieldDetail() {
        ApiFieldInterface apiInterface = ApiClient.getClient().create(ApiFieldInterface.class);
        ApiFieldService apiService = new ApiFieldService(apiInterface);

        apiService.getFieldById(fieldId, new ApiFieldService.ApiCallback<Field>() {
            @Override
            public void onSuccess(Field field) {
                if (field == null) {
                    Toast.makeText(getContext(), "Không tìm thấy sân!", Toast.LENGTH_SHORT).show();
                    return;
                }

                txtName.setText(field.getName());
                txtAddress.setText("Địa chỉ: " + field.getLocation());
                txtPhone.setText("Sức chứa: " + field.getCapacity());
                txtType.setText("Loại sân: " + field.getType());
                txtPrice.setText("Giá thuê: " + field.getPrice() + " VND");
                txtDescription.setText("Mô tả: " + field.getDescription());

                Glide.with(requireContext())
                        .load(field.getImages())
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(imgField);

                btnBooking.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), BookingActivity.class);
                    intent.putExtra("name", field.getName());
                    intent.putExtra("address", field.getLocation());
                    intent.putExtra("number", "Liên hệ sân");
                    intent.putExtra("image_url", field.getImages());
                    startActivity(intent);
                    dismiss();
                });

                btnDirection.setOnClickListener(v -> {
                    String uri = "google.navigation:q=" + latitude + "," + longitude;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");

                    if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(getContext(), "Không tìm thấy Google Maps!", Toast.LENGTH_SHORT).show();
                    }
                });

                Log.d("API_FIELD", "Field loaded: " + field.getName());
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu sân!", Toast.LENGTH_SHORT).show();
                Log.e("API_FIELD", "Lỗi khi gọi API: ", t);
            }
        });
    }
}
