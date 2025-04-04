package com.example.btl.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btl.R;
import com.example.btl.adapters.BookingHistoryAdapter;
import com.example.btl.utils.BookingDatabaseHelper;
import com.example.btl.models.TimeSlot;
import java.util.List;

public class BookedFieldsFragment extends Fragment {
    private RecyclerView recyclerView;
    private BookingHistoryAdapter adapter;
    private TextView txtNoBookings;
    private BookingDatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booked_fields, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBookedSlots);
        txtNoBookings = view.findViewById(R.id.txtNoBookings);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new BookingDatabaseHelper(getContext());
        databaseHelper.open();


        return view;
    }
    }

