package com.saibaba.sihpoliceapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.saibaba.sihpoliceapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddCriminal extends Fragment {
ImageView cimage;
EditText cname,cfname,cage,cdetails,ctype;
TextView cyes,cno;
Button cnextbtn;

    public AddCriminal() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_add_criminal, container, false);
        cimage=root.findViewById(R.id.cimage);
        cname=root.findViewById(R.id.cname);
        cfname=root.findViewById(R.id.cfname);
        cage=root.findViewById(R.id.cage);
        cdetails=root.findViewById(R.id.cdetails);
        ctype=root.findViewById(R.id.ctype);
        cyes=root.findViewById(R.id.cyes);
        cno=root.findViewById(R.id.cno);
        cnextbtn=root.findViewById(R.id.cnextbtn);
        return root;
    }

}
