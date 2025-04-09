package com.example.btl.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        addMarker(1, "Sân cầu lông", 21.007117, 105.838497, "Cầu lông");
        addMarker(2, "Sân tennis", 20.998875, 105.875770, "Tenis");
        addMarker(3, "Sân cầu lông 2", 21.003050, 105.856908, "Cầu lông");
        addMarker(4, "Sân PickelBall", 21.014328, 105.826653, "Pick");
        addMarker(5, "Sân tennis 2", 21.039722, 105.819184, "Tenis");
        addMarker(6, "Sân PickelBall 2", 21.062431, 105.795280, "Pick");
        addMarker(7, "Sân cầu lông 3", 21.005000, 105.828800, "Cầu lông");
        addMarker(8, "Sân tennis 3", 20.937428, 105.752365, "Tenis");
        addMarker(9, "Sân cầu lông 4", 20.901511, 105.773994, "Cầu lông");
        addMarker(10, "Sân tennis 4", 20.951335, 105.794164, "Tenis");
        addMarker(11, "Sân bóng đá", 20.949572, 105.774509, "Bóng đá");
        addMarker(12, "Sân bóng bàn ", 20.980188, 105.687047, "Bóng bàn");
    }


    private void addMarker(int fieldId, String name, double lat, double lng, String type) {
        int iconResId;

        switch (type.toLowerCase()) {
            case "cầu lông":
                iconResId = R.drawable.ic_badminton;
                break;
            case "tenis":
                iconResId = R.drawable.ic_tennis;
                break;
            case "pick":
            case "pickelball":
                iconResId = R.drawable.ic_pickleball;
                break;
            case "bóng đá":
                iconResId = R.drawable.ic_football;
                break;
            case "bóng bàn":
                iconResId = R.drawable.ic_table_tennis;
                break;
            default:
                return; // Không thêm marker nếu loại sân không hợp lệ
        }

        BitmapDescriptor icon = resizeIcon(iconResId, 72, 72); // hoặc 48x48 nếu muốn nhỏ hơn

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(name)
                .snippet("Nhấn để xem chi tiết")
                .icon(icon));

        if (marker != null) {
            marker.setTag(fieldId);
        }
    }

    private BitmapDescriptor resizeIcon(int resId, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), resId);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
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
