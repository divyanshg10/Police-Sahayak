package com.saibaba.sihpoliceapp.ui;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saibaba.sihpoliceapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFIR extends Fragment {
    private TextView pending,disposed,both;
    String status="";

    public StatusFIR() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_status_fir, container, false);
        pending=root.findViewById(R.id.pending);
        disposed=root.findViewById(R.id.disposed);
        both=root.findViewById(R.id.both);
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "pending";
                pending.setBackground(getResources().getDrawable(R.drawable.bg_edittextselected));
                disposed.setBackground(getResources().getDrawable(R.drawable.bg_edittext));
                both.setBackground(getResources().getDrawable(R.drawable.bg_edittext));
            }
        });
        disposed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "disposed";
                disposed.setBackground(getResources().getDrawable(R.drawable.bg_edittextselected));
                pending.setBackground(getResources().getDrawable(R.drawable.bg_edittext));
                both.setBackground(getResources().getDrawable(R.drawable.bg_edittext));

            }
        });
        both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = "both";
                both.setBackground(getResources().getDrawable(R.drawable.bg_edittextselected));
                disposed.setBackground(getResources().getDrawable(R.drawable.bg_edittext));
                pending.setBackground(getResources().getDrawable(R.drawable.bg_edittext));
            }
        });
        return  root;

    }

}
