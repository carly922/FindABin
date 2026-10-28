package com.findabin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set default fragment to MapFragment on app launch
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MapFragment())
                    .commit();
        }

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> getSupportFragmentManager().popBackStack());

        Button mapViewButton = findViewById(R.id.mapView);
        if (mapViewButton != null) {
            mapViewButton.setOnClickListener(v -> loadFragment(new MapFragment()));
        }

        Button listViewButton = findViewById(R.id.listView);
        if (listViewButton != null) {
            listViewButton.setOnClickListener(v -> loadFragment(new ListFragment()));
        }

        Button profileViewButton = findViewById(R.id.profileView);
        if (profileViewButton != null) {
            profileViewButton.setOnClickListener(v -> loadFragment(new ProfileFragment()));
        }

        FloatingActionButton addBinButton = findViewById(R.id.addBin);
        if (addBinButton != null) {
            addBinButton.setOnClickListener(v -> loadFragment(new AddABin()));
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
