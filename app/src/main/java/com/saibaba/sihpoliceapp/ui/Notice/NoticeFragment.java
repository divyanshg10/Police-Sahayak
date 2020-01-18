package com.saibaba.sihpoliceapp.ui.Notice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;

public class NoticeFragment extends Fragment {

    private NoticeViewModel sendViewModel;
EditText ntitle,ndetails;
Button nsave;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(NoticeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notice, container, false);
ntitle=root.findViewById(R.id.ntitle);
ndetails=root.findViewById(R.id.ndetails);
nsave=root.findViewById(R.id.nsave);
        return root;
    }
}