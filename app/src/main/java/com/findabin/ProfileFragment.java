package com.findabin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button myRatings = view.findViewById(R.id.myRatings);
        //myRatings.setOnClickListener(v -> openFragment(new Ratings()));

        Button reportConcern = view.findViewById(R.id.reportConcern);
        reportConcern.setOnClickListener(v -> openFragment(new ReportConcern()));

        Button settings = view.findViewById(R.id.settings);
        //settings.setOnClickListener(v -> openFragment(new Settings()));

    }

    private void openFragment(Fragment fragment){
        getParentFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit();
    }

}
