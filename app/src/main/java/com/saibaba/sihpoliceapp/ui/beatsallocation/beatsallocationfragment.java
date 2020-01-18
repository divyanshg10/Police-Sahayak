package com.saibaba.sihpoliceapp.ui.beatsallocation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.saibaba.sihpoliceapp.BeatsAllocation.BeatsMapsActivity;
import com.saibaba.sihpoliceapp.Constants;
import com.saibaba.sihpoliceapp.R;

import java.util.HashMap;

public class beatsallocationfragment extends Fragment {

    private beatsallocationviewModel mBeatsallocationviewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getActivity().startActivity(new Intent(getActivity(), BeatsMapsActivity.class));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBeatsallocationviewModel =
                ViewModelProviders.of(this).get(beatsallocationviewModel.class);
        View root = inflater.inflate(R.layout.fragment_beatsallocation, container, false);

        Button allocate=root.findViewById(R.id.allocateButton);
        allocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> hashMap = Constants.getDataFromSharedPreferences(getActivity());
                if(hashMap.get(Constants.USER_LEVEL).equals("3")){
                    Snackbar.make(v,"You are not allowed",Snackbar.LENGTH_SHORT).show();
                }else{
                    getActivity().startActivity(new Intent(getActivity(),BeatsMapsActivity.class));
                }
            }
        });
        return root;
    }
}