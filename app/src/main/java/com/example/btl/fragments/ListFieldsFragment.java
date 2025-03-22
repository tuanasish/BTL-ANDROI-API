package com.example.btl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.adapters.FieldAdapter;
import com.example.btl.models.Field;
import java.util.ArrayList;
import java.util.List;
public class ListFieldsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FieldAdapter fieldAdapter;
    private List<Field> fieldList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_fields, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fieldList = new ArrayList<>();
        fieldList.add(new Field("Sân số 1", "123 Đường A, Quận B", "0386518124", R.drawable.field1, 21.024574557786874, 105.79103836508581));
        fieldList.add(new Field("Sân số 2", "456 Đường B, Quận C", "0382714335", R.drawable.field2, 21.024574557786874, 105.79103836508581)); // Thêm tọa độ
        fieldList.add(new Field("Sân số 3", "123 Đường A, Quận B", "0386889223", R.drawable.field3, 21.024574557786874, 105.79103836508581)); // Thêm tọa độ
        fieldList.add(new Field("Sân số 4", "456 Đường B, Quận C", "0386125980", R.drawable.field4, 21.024574557786874, 105.79103836508581)); // Thêm tọa độ
        fieldList.add(new Field("Sân số 5", "456 Đường B, Quận C", "0359109677", R.drawable.field5, 21.024574557786874, 105.79103836508581)); // Thêm tọa độ

        fieldAdapter = new FieldAdapter(getContext(), fieldList);
        recyclerView.setAdapter(fieldAdapter);

        return view;
    }
}