package com.saibaba.sihpoliceapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saibaba.sihpoliceapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CriminalDetails extends Fragment {


    public CriminalDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_criminal_details, container, false);
    }

}
