package com.saibaba.sihpoliceapp.ui.beatsallocation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;

public class beatsallocationfragment extends Fragment {

    private beatsallocationviewModel mBeatsallocationviewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBeatsallocationviewModel =
                ViewModelProviders.of(this).get(beatsallocationviewModel.class);
        View root = inflater.inflate(R.layout.fragment_beatsallocation, container, false);

        return root;
    }
}