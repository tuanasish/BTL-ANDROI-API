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

        // Tạo adapter với OnItemClickListener
        fieldAdapter = new FieldAdapter(getContext(), fieldList, new FieldAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Field field) {
                // Khi nhấn vào một sân, truyền fieldId sang FieldDetailActivity
                Intent intent = new Intent(getContext(), FieldDetailActivity.class);
                intent.putExtra("FIELD_ID", field.getField_id());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(fieldAdapter);

        ApiFieldInterface apiFieldInterface = ApiClient.getClient().create(ApiFieldInterface.class);
        apiFieldService = new ApiFieldService(apiFieldInterface);

        // Lấy dữ liệu field
        loadFields();

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
