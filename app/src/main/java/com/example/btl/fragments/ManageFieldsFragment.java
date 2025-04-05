package com.example.btl.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.btl.R;
import com.example.btl.activities.AddFieldActivity;
import com.example.btl.activities.EditFieldActivity;
import com.example.btl.adapters.FieldManagerAdapter;
import com.example.btl.models.Field;

import java.util.ArrayList;
import java.util.List;

public class ManageFieldsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FieldManagerAdapter fieldManagerAdapter;
    // Make the field list static to persist data between fragment instances
    private static List<Field> fieldList = new ArrayList<>();
    private Button btnAddField;

    private static final int REQUEST_CODE_EDIT = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_manage_fields, container, false);

        // Ánh xạ các phần tử
        recyclerView = view.findViewById(R.id.recyclerViewFields);
        btnAddField = view.findViewById(R.id.btnAddField);

        // Khởi tạo adapter với dữ liệu từ static list
        fieldManagerAdapter = new FieldManagerAdapter(getContext(), fieldList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(fieldManagerAdapter);

        // Sự kiện nhấn nút "Thêm sân"
        btnAddField.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddFieldActivity.class);
            startActivity(intent);
        });

        // Add sample fields
        addSampleFields();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the adapter when returning to the fragment
        if (fieldManagerAdapter != null) {
            fieldManagerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EDIT && resultCode == getActivity().RESULT_OK && data != null) {
            Field updatedField = (Field) data.getSerializableExtra("UPDATED_FIELD");
            if (updatedField != null) {
                // Cập nhật lại sân trong danh sách
                for (int i = 0; i < fieldList.size(); i++) {
                    if (fieldList.get(i).getField_id() == updatedField.getField_id()) {
                        fieldList.set(i, updatedField);
                        break;
                    }
                }
                fieldManagerAdapter.notifyDataSetChanged();
            }
        }
    }

    // Static method to add fields that can be called from any activity
    public static void addField(Field newField) {
        if (newField != null) {
            fieldList.add(newField);
        }
    }

    // Public method to update the list from activities
    public void updateFieldList(Field newField) {
        if (newField != null) {
            // Use the static method
            addField(newField);
            // Update the adapter
            if (fieldManagerAdapter != null) {
                fieldManagerAdapter.notifyDataSetChanged();
            }
        }
    }

    // Optional: Method to get all fields (useful for testing or debugging)
    public static List<Field> getAllFields() {
        return fieldList;
    }

    // Method to add sample fields to the list
    private void addSampleFields() {
        if (fieldList.isEmpty()) {  // Only add sample data if the list is empty
            fieldList.add(new Field(10, "Mô tả sân 1", 1, "field1", "Địa chỉ 1", "Sân 1", 500000, "Bóng đá"));
            fieldList.add(new Field(15, "Mô tả sân 2", 2, "field2", "Địa chỉ 2", "Sân 2", 400000, "Tennis"));
            fieldList.add(new Field(12, "Mô tả sân 3", 3, "field3", "Địa chỉ 3", "Sân 3", 300000, "Cầu lông"));
            fieldList.add(new Field(20, "Mô tả sân 4", 4, "field4", "Địa chỉ 4", "Sân 4", 600000, "Bóng rổ"));
        }
    }
}
