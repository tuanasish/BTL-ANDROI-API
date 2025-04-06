package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btl.R;
import com.example.btl.activities.AddFieldActivity;
import com.example.btl.adapters.FieldManagerAdapter;
import com.example.btl.api.ApiClient;
import com.example.btl.api.ApiFieldInterface;
import com.example.btl.api.ApiFieldService;
import com.example.btl.models.Field;

import java.util.ArrayList;
import java.util.List;

public class ManageFieldsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FieldManagerAdapter fieldManagerAdapter;
    private List<Field> fieldList = new ArrayList<>();
    private Button btnAddField;
    private ApiFieldService apiFieldService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_fields, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFields);
        btnAddField = view.findViewById(R.id.btnAddField);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fieldManagerAdapter = new FieldManagerAdapter(getContext(), fieldList);
        recyclerView.setAdapter(fieldManagerAdapter);

        // Init API
        ApiFieldInterface apiFieldInterface = ApiClient.getClient().create(ApiFieldInterface.class);
        apiFieldService = new ApiFieldService(apiFieldInterface);

        // Load data từ API
        loadFieldsFromApi();

        btnAddField.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddFieldActivity.class);
            startActivityForResult(intent, 101);
        });

        return view;
    }


    private void loadFieldsFromApi() {
        apiFieldService.getAllFields(new ApiFieldService.ApiCallback<List<Field>>() {
            @Override
            public void onSuccess(List<Field> fields) {
                fieldList.clear();
                fieldList.addAll(fields);
                fieldManagerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable t) {
                Log.e("API_ERROR", "Lỗi gọi API: " + t.getMessage());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFieldsFromApi(); // Refresh lại dữ liệu khi quay về Fragment
    }
}
