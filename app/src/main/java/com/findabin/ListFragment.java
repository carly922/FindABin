package com.findabin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.findabin.data.BinRepository;
import com.findabin.model.Bin;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private final BinRepository binRepository = new BinRepository();
    private List<Bin> bins = new ArrayList<>();
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);

        }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*String[] bins = {
                "Bin location 1 - 5mi",
                "Bin location 2 - 7mi",
                "Bin location 3 - 9mi",
                "Bin location 4 - 10mi",
                "Bin location 5 - 10mi",
        };*/

        listView = view.findViewById(R.id.binListView);
        /*listView.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, bins));*/

        listView.setOnItemClickListener((parent, rowView, position, id) -> {

            Bin bin = bins.get(position);

            openBin(bin.getId());







                /*requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new BinFragment())
                        .addToBackStack(null)
                        .commit());*/


        });

        loadBins();
    }

    private void loadBins(){
        binRepository.getAllBins(new BinRepository.Callback<List<Bin>>() {
            @Override
            public void onSuccess(List<Bin> result) {
                if(!isAdded()){
                    return;
                }

                bins = result;

                List<String> names = new ArrayList<>();

                for(Bin bin: bins){
                    names.add(bin.getName());
                }

                listView.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, names));
            }

            @Override
            public void onError(Exception e) {
                Log.e("BinList", "error loading bins", e);
            }
        });
    }

    private void openBin(String binId){
        BinFragment fragment = new BinFragment();
        Bundle args = new Bundle();

        args.putString("binId", binId);

        fragment.setArguments(args);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
