package com.findabin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BinFragment extends Fragment {

    private View activityAddButton;
    private int previousAddButtonVisibility;

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
