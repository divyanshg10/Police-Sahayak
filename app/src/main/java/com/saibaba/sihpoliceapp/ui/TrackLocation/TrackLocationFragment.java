package com.saibaba.sihpoliceapp.ui.TrackLocation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;

public class TrackLocationFragment extends Fragment {

    private TrackLocationModel mTrackLocationModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mTrackLocationModel =
                ViewModelProviders.of(this).get(TrackLocationModel.class);
        View root = inflater.inflate(R.layout.fragment_tracklocation, container, false);

        return root;
    }
}