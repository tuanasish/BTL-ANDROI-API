package com.example.btl.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.btl.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private List<LatLng> locationPoints;
    private List<String> locationNames;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        locationPoints = new ArrayList<>();
        locationPoints.add(new LatLng(20.993712, 105.878259));
        locationPoints.add(new LatLng(20.991451, 105.865873));
        locationPoints.add(new LatLng(21.003284, 105.856713));
        locationNames = new ArrayList<>();
        locationNames.add("Địa điểm 1");
        locationNames.add("Địa điểm 2");
        locationNames.add("Địa điểm 3");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        mMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    } else {
                        if (!locationPoints.isEmpty()) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationPoints.get(0), 15));
                        }
                    }
                });

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        addMarkersToMap();
        mMap.setOnInfoWindowClickListener(this);

        mMap.setOnMarkerClickListener(marker -> {
            marker.showInfoWindow();
            return true;
        });
    }

    private void addMarkersToMap() {
        for (int i = 0; i < locationPoints.size(); i++) {
            LatLng point = locationPoints.get(i);
            String title = i < locationNames.size() ? locationNames.get(i) : "Địa điểm " + (i + 1);

            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title(title)
                    .snippet("Nhấn để xem chỉ đường"));
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        LatLng position = marker.getPosition();
        startNavigation(position.latitude, position.longitude, marker.getTitle());
    }

    private void startNavigation(double latitude, double longitude, String destinationName) {
        Uri navigationUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigationUri);
        navigationIntent.setPackage("com.google.android.apps.maps");

        if (navigationIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(navigationIntent);
        }
    }
}
