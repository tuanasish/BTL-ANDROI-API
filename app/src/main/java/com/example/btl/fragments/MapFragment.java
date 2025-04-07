package com.example.btl.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.btl.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnInfoWindowClickListener(this);

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            LatLng center = new LatLng(21.003284, 105.856713); // Vị trí mặc định
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 14));
        });

        addStaticMarkers();
    }

    private void addStaticMarkers() {
        addMarker(1, "Sân A", 21.003284, 105.856713);
        addMarker(2, "Sân B", 20.980721, 105.787315);
        addMarker(3, "Sân C", 21.027764, 105.834160);
        addMarker(4, "Sân D", 21.010000, 105.850000);
        addMarker(5, "Sân E", 21.011000, 105.851000);
        addMarker(6, "Sân F", 21.012000, 105.852000);
        addMarker(7, "Sân G", 21.013000, 105.853000);
        addMarker(8, "Sân H", 21.014000, 105.854000);
        addMarker(9, "Sân I", 21.015000, 105.855000);
        addMarker(10, "Sân J", 21.016000, 105.856000);
    }

    private void addMarker(int fieldId, String name, double lat, double lng) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(name)
                .snippet("Nhấn để xem chi tiết"));
        if (marker != null) {
            marker.setTag(fieldId); // Gắn fieldId vào marker
        }
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Object tag = marker.getTag();
        if (tag instanceof Integer) {
            int fieldId = (int) tag;
            LatLng position = marker.getPosition();
            FieldDetailBottomSheet bottomSheet = FieldDetailBottomSheet.newInstance(fieldId, position.latitude, position.longitude);
            bottomSheet.show(getChildFragmentManager(), "field_detail");
        }
    }
}
