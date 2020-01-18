package com.saibaba.sihpoliceapp.ui.beatsallocation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.BeatsAllocation.BeatsMapsActivity;
import com.saibaba.sihpoliceapp.R;

public class beatsallocationfragment extends Fragment {

    private beatsallocationviewModel mBeatsallocationviewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().startActivity(new Intent(getActivity(), BeatsMapsActivity.class));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBeatsallocationviewModel =
                ViewModelProviders.of(this).get(beatsallocationviewModel.class);
        View root = inflater.inflate(R.layout.fragment_beatsallocation, container, false);

        return root;
    }
}