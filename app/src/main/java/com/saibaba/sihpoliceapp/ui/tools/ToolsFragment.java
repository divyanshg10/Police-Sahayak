package com.saibaba.sihpoliceapp.ui.tools;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;
import com.saibaba.sihpoliceapp.ui.StatusFIR;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

public class ToolsFragment extends Fragment {
    EditText firno,fdate,tdate;
    String[] station,district;
    SearchableSpinner district_spinner,station_spinner;
    private ToolsViewModel toolsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);

        station_spinner=root.findViewById(R.id.station_spinner);
        district_spinner=root.findViewById(R.id.district_spinner);
        firno=root.findViewById(R.id.firno);
        fdate=root.findViewById(R.id.fdate);
        tdate=root.findViewById(R.id.tdate);

        district = new String[]{"Araria", "Arwal", "Aurangabad", "Banka", "Begusarai", "Bhagalpur", "Bhojpur", "Buxar", "Darbhanga", "Gaya", "Gopalganj",
                "Jamui", "Jehanabad", "Kaimur", "Katihar", "Khagaria", "Kishanganj", "Lakhisarai", "Madhepura", "Madhubani", "Munger", "Muzaffarpur",
                "Nalanda", "Nawada", "Pashchim Champaran", "Patna", "Purbi Champaran", "Purnia", "Rohtas", "Saharsa", "Samastipur", "Saran", "Sheikhpura",
                "Sitamarhi", "Siwan", "Supaul", "Vaishali"};
        station = new String[]{"Agamkuan", "Bihta", "Barh", "Digha", "Dhanarua", "Hathidah", "Chowk", "Maner", "Punpun", "S K Puri", "Sahpur"};
        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, station);
        station_spinner.setAdapter(stationAdapter);
        ArrayAdapter<String> districtsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, district);
        district_spinner.setAdapter(districtsAdapter);
root.findViewById(R.id.nextbtn).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
       StatusFIR firStatus=new StatusFIR();
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment,firStatus);
        fragmentTransaction.commit();
    }
});

        return root;
    }


}
