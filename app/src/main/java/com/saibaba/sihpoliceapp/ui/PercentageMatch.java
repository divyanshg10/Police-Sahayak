package com.saibaba.sihpoliceapp.ui;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.saibaba.sihpoliceapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PercentageMatch extends Fragment {
TextView vdetails;

    public PercentageMatch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_percentage_match, container, false);
        PieChart pieChart=root.findViewById(R.id.pieChart);
        pieChart.setUsePercentValues(true);
        Description desc=new Description();
        desc.setText("% Match With Existing Criminal");
        desc.setTextSize(40f);
        pieChart.setDescription(desc);
        pieChart.setTransparentCircleRadius(25f);
        List<PieEntry> value=new ArrayList<>();
        value.add(new PieEntry(40f,"Match"));
        value.add(new PieEntry(60f,"Unmatch"));
        PieDataSet pieDataSet=new PieDataSet(value,"");
        PieData pieData=new PieData(pieDataSet);
pieChart.setData(pieData);
pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
pieChart.animateXY(1400,1400);
        root.findViewById(R.id.vdetails).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CriminalDetails cd=new CriminalDetails();
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment,cd);
                fragmentTransaction.commit();
            }
        });
        return root;
    }

}
