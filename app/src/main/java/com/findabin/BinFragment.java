package com.findabin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import com.findabin.data.BinRepository;
import com.findabin.model.Bin;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BinFragment extends Fragment {

    private View activityAddButton;
    private int previousAddButtonVisibility;

    private BinRepository binRepository = new BinRepository();

    public BinFragment() {
        super(R.layout.fragment_bin_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // This layout has its own add icon, so hide the activity's floating one.
        activityAddButton = requireActivity().findViewById(R.id.addBin);
        if (activityAddButton != null) {
            previousAddButtonVisibility = activityAddButton.getVisibility();
            activityAddButton.setVisibility(View.GONE);
        }

        final TextView binTitle = view.findViewById(R.id.binTitle);
        final TextView binDescription = view.findViewById(R.id.binDescription);
        final AppCompatImageButton binDirectionsButton = view.findViewById(R.id.binDirectionsButton);
        final AppCompatImageButton addReviewButton = view.findViewById(R.id.binAddButton);

        String binId = requireArguments().getString("binId");

        binRepository.getBin(binId, new BinRepository.Callback<Bin>() {
            @Override
            public void onSuccess(Bin result) {
                if (!isAdded()) return;
                binTitle.setText(result.getName());
                binDescription.setText(result.getDescription());

                if (result.getLocation() != null) {
                    double lat = result.getLocation().getLatitude();
                    double lng = result.getLocation().getLongitude();
                    binDirectionsButton.setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + lat + "," + lng));
                        startActivity(intent);
                    });

                    SupportMapFragment mapPreview = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.binMapPreview);
                    if (mapPreview != null) {
                        mapPreview.getMapAsync(map -> {
                            LatLng pos = new LatLng(lat, lng);
                            map.addMarker(new MarkerOptions().position(pos).title(result.getName()));
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 16f));
                        });
                    }
                }

                addReviewButton.setOnClickListener(v ->{
                    Bundle bundle = new Bundle();
                    bundle.putString("binId", binId);


                    Fragment ratingsFragment = new RatingsFragment();

                    ratingsFragment.setArguments(bundle);
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, ratingsFragment)
                            .addToBackStack(null)
                            .commit();
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e("LoadDetail", "Failed to load bin details", e);
            }
        });

    }



    @Override
    public void onDestroyView() {
        if (activityAddButton != null) {
            activityAddButton.setVisibility(previousAddButtonVisibility);
            activityAddButton = null;
        }
        super.onDestroyView();
    }
}
