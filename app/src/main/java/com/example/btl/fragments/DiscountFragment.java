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
import com.example.btl.adapters.DiscountAdapter;
import com.example.btl.models.Discount;

import java.util.ArrayList;
import java.util.List;

public class DiscountFragment extends Fragment {

    private RecyclerView recyclerView;
    private DiscountAdapter discountAdapter;
    private List<Discount> discountList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discount, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDiscount);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        discountList = new ArrayList<>();

        discountList.add(new Discount(R.drawable.discount1,1));
        discountList.add(new Discount(R.drawable.discount2,2));
        discountList.add(new Discount(R.drawable.discount3,3));

        discountAdapter = new DiscountAdapter(getContext(), discountList);
        recyclerView.setAdapter(discountAdapter);

        return view;
    }
}
