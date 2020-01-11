package com.saibaba.sihpoliceapp.ui.home;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;
import com.saibaba.sihpoliceapp.ui.PercentageMatch;
import com.saibaba.sihpoliceapp.ui.gallery.GalleryFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
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