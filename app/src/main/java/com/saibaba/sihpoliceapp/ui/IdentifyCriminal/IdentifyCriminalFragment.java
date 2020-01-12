package com.saibaba.sihpoliceapp.ui.IdentifyCriminal;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;
import com.saibaba.sihpoliceapp.ui.PercentageMatch;

public class IdentifyCriminalFragment extends Fragment {

    private IdentifyCriminalModel mIdentifyCriminalModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mIdentifyCriminalModel =
                ViewModelProviders.of(this).get(IdentifyCriminalModel.class);
        View root = inflater.inflate(R.layout.fragment_identifycriminal, container, false);
root.findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PercentageMatch gf=new PercentageMatch();
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment,gf);
        fragmentTransaction.commit();
    }
});
        return root;
    }
}