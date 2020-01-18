package com.saibaba.sihpoliceapp.ui.TrackLocation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;
import com.saibaba.sihpoliceapp.services.LocationServiceStatus;
import com.saibaba.sihpoliceapp.services.locationService;

public class TrackLocationFragment extends Fragment {

    private TrackLocationModel mTrackLocationModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mTrackLocationModel =
                ViewModelProviders.of(this).get(TrackLocationModel.class);
        View root = inflater.inflate(R.layout.fragment_tracklocation, container, false);
        final Button button=root.findViewById(R.id.capture);
//        if(Loca)
        if(locationService.getLocationServiceStatus()== LocationServiceStatus.RUNNNING){
            button.setText("Stop Capturing Location");
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().stopService(new Intent(getContext(),locationService.class));
//                    button.setText("Capture Location");
//                }
//            });
        }else{
            button.setText("Capture Location");
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    startService();
//                    getActivity().startService(new Intent(getContext(), locationService.class));
//                    button.setText("Stop Capturing Location");
//                }
//            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button.getText().toString().equals("Capture Location")){
                    getActivity().startService(new Intent(getContext(), locationService.class));
                    button.setText("Stop Capturing Location");
                }else{
                    getActivity().stopService(new Intent(getContext(),locationService.class));
                    button.setText("Capture Location");
                }
            }
        });
        return root;
    }
    private void stopService(){

    }
}