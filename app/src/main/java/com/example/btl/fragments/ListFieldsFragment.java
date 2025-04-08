package com.example.btl.fragments;

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
import com.example.btl.adapters.FieldAdapter;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiFieldService;
import com.example.btl.models.Field;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class ListFieldsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FieldAdapter fieldAdapter;
    private List<Field> fieldList;
    private ApiFieldService apiFieldService;

    private Chip chipTennis, chipBadminton, chipFootball, chipTableTennis, chipPickleball, chipSortByPrice;
    private boolean isPriceAsc = true; // Toggle sắp xếp giá

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_fields, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fieldList = new ArrayList<>();
        fieldAdapter = new FieldAdapter(getContext(), fieldList);
        recyclerView.setAdapter(fieldAdapter);

        chipTennis = view.findViewById(R.id.chipTennis);
        chipBadminton = view.findViewById(R.id.chipBadminton);
        chipFootball = view.findViewById(R.id.chipFootball);
        chipTableTennis = view.findViewById(R.id.chipTableTennis);
        chipPickleball = view.findViewById(R.id.chipPickleball);
        chipSortByPrice = view.findViewById(R.id.chipSortByPrice);

        ApiFieldInterface apiFieldInterface = ApiClient.getClient().create(ApiFieldInterface.class);
        apiFieldService = new ApiFieldService(apiFieldInterface);

        loadFields(); // mặc định load toàn bộ sân

        chipTennis.setOnClickListener(v -> filterByType("tennis"));
        chipBadminton.setOnClickListener(v -> filterByType("badminton"));
        chipFootball.setOnClickListener(v -> filterByType("football"));
        chipTableTennis.setOnClickListener(v -> filterByType("tabletennis"));
        chipPickleball.setOnClickListener(v -> filterByType("pickleball"));

        chipSortByPrice.setOnClickListener(v -> {
            String sortType = isPriceAsc ? "asc" : "desc";
            sortFieldsByPrice(sortType);
            chipSortByPrice.setText(isPriceAsc ? "Giá ↑" : "Giá ↓");
            isPriceAsc = !isPriceAsc;
        });

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
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi khi tải danh sách sân", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterByType(String type) {
        apiFieldService.getFieldsByType(type, new ApiFieldService.ApiCallback<List<Field>>() {
            @Override
            public void onSuccess(List<Field> result) {
                fieldList.clear();
                fieldList.addAll(result);
                fieldAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Đã lọc sân: " + type, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Lỗi khi lọc sân: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sortFieldsByPrice(String sortOrder) {
        apiFieldService.sortFields(sortOrder, new ApiFieldService.ApiCallback<List<Field>>() {
            @Override
            public void onSuccess(List<Field> result) {
                fieldList.clear();
                fieldList.addAll(result);
                fieldAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Đã sắp xếp theo giá " + (sortOrder.equals("asc") ? "tăng" : "giảm"), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Lỗi sắp xếp: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
