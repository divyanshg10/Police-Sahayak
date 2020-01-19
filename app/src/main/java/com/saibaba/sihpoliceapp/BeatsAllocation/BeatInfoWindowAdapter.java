package com.saibaba.sihpoliceapp.BeatsAllocation;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.saibaba.sihpoliceapp.Modal.BeatDetail;
import com.saibaba.sihpoliceapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class BeatInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    View view;
    private static final String TAG = "BeatInfoWindowAdapter";
    private ArrayList<BeatDetail> arrayList;
    private ImageView subimg;
    private TextView subname;
    private Button subcall;

    public BeatInfoWindowAdapter(ArrayList<BeatDetail> arrayList, Context context) {
        this.arrayList = arrayList;
        Log.d(TAG, "BeatInfoWindowAdapter: "+arrayList);
        view = LayoutInflater.from(context).inflate(R.layout.element_info_window,null);
        subimg=view.findViewById(R.id.subimage);
        subname=view.findViewById(R.id.textViewName);
        subcall=view.findViewById(R.id.dialButton);
    }

    @Override
    public View getInfoWindow(Marker marker) {
//        String uid=marker.getTitle();
//        for(int i=0;i<arrayList.size();i++){
//            if(arrayList.get(i).getUid().equals(uid)){
//                Picasso.get().load(arrayList.get(i).getUserImg()).into(subimg);
//                subcall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.d(TAG, "onClick: clicked");
//                    }
//                });
//                subname.setText(arrayList.get(i).getName());
//                break;
//            }
//        }
//        Log.d(TAG, "getInfoWindow: "+marker.getTitle());
//        return view;
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
