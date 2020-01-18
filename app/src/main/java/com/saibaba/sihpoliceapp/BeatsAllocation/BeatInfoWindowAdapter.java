package com.saibaba.sihpoliceapp.BeatsAllocation;

import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class BeatInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private ArrayList<String> arrayList;

    public BeatInfoWindowAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public View getInfoWindow(Marker marker) {
//        Inflater inflater=Infa
//        View view=Inflater.
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
