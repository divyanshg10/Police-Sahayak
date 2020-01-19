package com.saibaba.sihpoliceapp.ui.beatsallocation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saibaba.sihpoliceapp.BeatsAllocation.BeatsMapsActivity;
import com.saibaba.sihpoliceapp.Constants;
import com.saibaba.sihpoliceapp.R;

import java.util.HashMap;

public class beatsallocationfragment extends Fragment {

    private static final String TAG = "beatsallocationfragment";
    private beatsallocationviewModel mBeatsallocationviewModel;
    private TextView textViewAllocate;
    HashMap<String,String> hashMap;

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
        textViewAllocate= root.findViewById(R.id.textViewAllocate);
        hashMap = Constants.getDataFromSharedPreferences(getActivity());
        Button allocate=root.findViewById(R.id.allocateButton);
        allocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hashMap.get(Constants.USER_LEVEL).equals("3")){
                    Snackbar.make(v,"You are not allowed",Snackbar.LENGTH_SHORT).show();
                }else{
                    getActivity().startActivity(new Intent(getActivity(),BeatsMapsActivity.class));
                }
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(!hashMap.get(Constants.USER_LEVEL).equals("3")){
            textViewAllocate.setVisibility(View.GONE);
        }else{
            FirebaseDatabase.getInstance().getReference().child("police").child(hashMap.get(Constants.USER_UID))
                    .child("allocation").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String slatitude=String.valueOf( dataSnapshot.child("latitude").getValue());
                    String slongitude=String.valueOf(dataSnapshot.child("longitude").getValue());
                    textViewAllocate.setText("you are allocated coordinates { "+slatitude+" , "+slongitude+" }");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled: "+databaseError.getMessage() );
                }
            });
        }
    }
}