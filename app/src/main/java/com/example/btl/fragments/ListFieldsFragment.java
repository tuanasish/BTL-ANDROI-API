package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.activities.FieldDetailActivity;
import com.example.btl.adapters.FieldAdapter;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiFieldService;
import com.example.btl.models.Field;

import java.util.ArrayList;
import java.util.List;

public class ListFieldsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FieldAdapter fieldAdapter;
    private List<Field> fieldList;
    private ApiFieldService apiFieldService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_fields, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fieldList = new ArrayList<>();

        fieldList.add(new Field(50, "Sân bóng đá 11 người, có mái che, đầy đủ trang thiết bị", 1, "field1_image.jpg", "Đường Nguyễn Văn Linh, TP.HCM", "Sân Bóng 11 Người", 500000, "Football"));
        fieldList.add(new Field(30, "Sân bóng đá 7 người, có đèn chiếu sáng, mặt sân cỏ nhân tạo", 2, "field2_image.jpg", "Đường Lý Thường Kiệt, TP.HCM", "Sân Bóng 7 Người", 350000, "Football"));
        fieldList.add(new Field(20, "Sân tennis đôi, mặt sân cứng, có khu vực chờ", 3, "field3_image.jpg", "Đường Trường Chinh, TP.HCM", "Sân Tennis", 200000, "Tennis"));
        fieldList.add(new Field(10, "Sân cầu lông, có đèn chiếu sáng, không gian thoáng đãng", 4, "field4_image.jpg", "Đường Phan Văn Trị, TP.HCM", "Sân Cầu Lông", 100000, "Badminton"));
        fieldList.add(new Field(25, "Sân bóng rổ, có bảng rổ, đèn chiếu sáng đủ", 5, "field5_image.jpg", "Đường Hồ Tùng Mậu, TP.HCM", "Sân Bóng Rổ", 150000, "Basketball"));

        fieldAdapter = new FieldAdapter(getContext(), fieldList);
        recyclerView.setAdapter(fieldAdapter);

//        ApiFieldInterface apiFieldInterface = ApiClient.getClient().create(ApiFieldInterface.class);
//        apiFieldService = new ApiFieldService(apiFieldInterface);
//
//        // Lấy dữ liệu field
//        loadFields();
        return view;
    }

    private void loadFields() {
        apiFieldService.getAllFields(new ApiFieldService.ApiCallback<List<Field>>() {
            @Override
            public void onSuccess(List<Field> fields) {
                fieldList.clear();
                fieldList.addAll(fields);
                fieldAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {
                Log.e("API_ERROR", "Loi: " + t.getMessage());
            }
        });
    }
}