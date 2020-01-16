package com.saibaba.sihpoliceapp.ui.IdentifyVehicle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;

public class IdentifyVehicle extends Fragment {

    private IdentifyVehicleViewModel mIdentifyVehicleViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mIdentifyVehicleViewModel =
                ViewModelProviders.of(this).get(IdentifyVehicleViewModel.class);
        View root = inflater.inflate(R.layout.fragment_identify_vehicle, container, false);

        return root;
    }
}