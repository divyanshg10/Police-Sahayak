package com.saibaba.sihpoliceapp.ui;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.saibaba.sihpoliceapp.Modal.Face;
import com.saibaba.sihpoliceapp.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class CriminalList extends Fragment {

    private static ViewPagerAdapter viewPagerAdapter;

    VerticalViewPager verticalViewPager;
    private HashMap<String, Face> hashMap;
    public CriminalList() {
        // Required empty public constructor
    }

    public CriminalList(Intent intent){
        hashMap=(HashMap<String,Face>)intent.getSerializableExtra("hashmap");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.activity_criminal_list, container, false);
        verticalViewPager=v.findViewById(R.id.viewPager_officers);
        viewPagerAdapter =new ViewPagerAdapter(getChildFragmentManager());

        for(Map.Entry<String,Face> entry : hashMap.entrySet()){
            viewPagerAdapter.addFragment(new PercentageMatch(entry.getValue()),"criminal");
        }

        verticalViewPager.setAdapter(viewPagerAdapter);

        return v;
    }

}
