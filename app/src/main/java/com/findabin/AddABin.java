package com.findabin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.findabin.data.BinRepository;
import com.findabin.model.Bin;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AddABin extends Fragment {

    private static final LatLng DEFAULT_LOCATION = new LatLng(28.6016, -81.2005);

    private GeoPoint pickedLocation;
    private GoogleMap previewMap;
    private View activityAddButton;


    public AddABin() {}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_a_bin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityAddButton = requireActivity().findViewById(R.id.addBin);
        if (activityAddButton != null) {
            activityAddButton.setVisibility(View.GONE);
        }

        final TextInputEditText binTitle = view.findViewById(R.id.binTitle);
        final TextInputEditText moreDetails = view.findViewById(R.id.moreDetails);
        final ChipGroup chipGroup = view.findViewById(R.id.chipGroup);
        final Button locationButton = view.findViewById(R.id.useLocationButton);
        final Button submitButton = view.findViewById(R.id.submit);

        final SearchView searchView = view.findViewById(R.id.searchView2);

        final BinRepository binRepository = new BinRepository();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mockView);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this::onPreviewMapReady);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus(); // hides the keyboard
                searchAddress(query.trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        locationButton.setOnClickListener(v -> {
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(requireContext(), "Location permission is needed", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(requireContext());
                    client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                            .addOnSuccessListener(location -> {
                                if (!isAdded()) return;
                                if (location != null) {
                                    pickedLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                                    showPin();
                                    Toast.makeText(requireContext(), "Location set", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Couldn't get your location, try again", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                if (!isAdded()) return;
                                Toast.makeText(requireContext(), "Couldn't get your location", Toast.LENGTH_SHORT).show();
                            });

                });

            submitButton.setOnClickListener(v -> {
                final List<Integer> ids = chipGroup.getCheckedChipIds();
                List<String> tags = new ArrayList<>();
                for(int id: ids ){
                    tags.add(((Chip) view.findViewById(id)).getText().toString().toLowerCase());
                }

                if(pickedLocation == null){
                    Toast.makeText(requireContext(), "Tap 'Use my current location' first", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(binTitle.getText().toString().trim().length() != 0){
                    Bin bin = new Bin();
                    bin.setName(binTitle.getText().toString());
                    bin.setDescription(moreDetails.getText().toString());
                    bin.setLocation(pickedLocation);
                    bin.setAcceptedTypes(tags);
                    bin.setCreatedBy(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    submitButton.setEnabled(false);
                    binRepository.addBin(bin, new BinRepository.Callback<Void>() {
                        @Override
                        public void onSuccess(Void result) {
                            if (!isAdded()) return;
                            Toast.makeText(requireContext(), "Bin added", Toast.LENGTH_SHORT).show();

                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_container, new MapFragment())
                                    .commit();
                        }

                        @Override
                        public void onError(Exception e) {
                            if (!isAdded()) return;
                            submitButton.setEnabled(true);
                            Toast.makeText(requireContext(), "Couldn't add bin: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    binTitle.setError("Required");
                    return;
                }
            });

    }


    @SuppressWarnings("deprecation")
    private void searchAddress(String query) {
        if (query.isEmpty()) return;

        if (!Geocoder.isPresent()) {
            Toast.makeText(requireContext(), "Address search isn't available on this device", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());

        new Thread(() -> {
            List<Address> results = null;
            try {
                results = geocoder.getFromLocationName(query, 1);
            } catch (IOException e) {
                Log.e("AddABin", "Geocoder failed", e);
            }

            final List<Address> finalResults = results;
            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                if (!isAdded()) return;
                if (finalResults == null || finalResults.isEmpty()) {
                    Toast.makeText(requireContext(), "Address not found", Toast.LENGTH_SHORT).show();
                    return;
                }
                Address address = finalResults.get(0);
                pickedLocation = new GeoPoint(address.getLatitude(), address.getLongitude());
                showPin();
                String label = address.getAddressLine(0) != null ? address.getAddressLine(0) : query;
                Toast.makeText(requireContext(), "Location set: " + label, Toast.LENGTH_LONG).show();
            });
        }).start();
    }

    private void onPreviewMapReady(GoogleMap map) {
        previewMap = map;
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setOnMapClickListener(latLng -> {});
        map.setOnMarkerClickListener(marker -> true);

        if (pickedLocation != null) {
            showPin();
            return;
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15f));

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationServices.getFusedLocationProviderClient(requireContext()).getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null && previewMap != null && pickedLocation == null) {
                        previewMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(location.getLatitude(), location.getLongitude()), 15f));
                    }
                });
    }

    private void showPin() {
        if (previewMap == null || pickedLocation == null) return;
        LatLng pos = new LatLng(pickedLocation.getLatitude(), pickedLocation.getLongitude());
        previewMap.clear();
        previewMap.addMarker(new MarkerOptions().position(pos));
        previewMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f));
    }

    @Override
    public void onDestroyView() {
        previewMap = null;
        if (activityAddButton != null) {
            activityAddButton.setVisibility(View.VISIBLE);
            activityAddButton = null;
        }
        super.onDestroyView();
    }
}