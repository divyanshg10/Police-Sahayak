package com.saibaba.sihpoliceapp.ui.change_status;

import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.saibaba.sihpoliceapp.R;
import com.saibaba.sihpoliceapp.ui.StatusFIR;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class change_status_Fragment extends Fragment {
    EditText firno,fdate,tdate;
    String[] station,district;
    SearchableSpinner district_spinner,station_spinner;
    final Calendar myCalendar = Calendar.getInstance();
    private change_status_ViewModel mChangestatusViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mChangestatusViewModel =
                ViewModelProviders.of(this).get(change_status_ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_change_status, container, false);

        station_spinner=root.findViewById(R.id.station_spinner);
        district_spinner=root.findViewById(R.id.district_spinner);
        firno=root.findViewById(R.id.firno);
        fdate=root.findViewById(R.id.fdate);
        tdate=root.findViewById(R.id.tdate);


        final DatePickerDialog.OnDateSetListener date = new
                DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(fdate);
                    }

                };

        fdate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(getActivity(), date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return false;
            }
        });
        final DatePickerDialog.OnDateSetListener date1 = new
                DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel(tdate);
                    }

                };
        tdate.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    new DatePickerDialog(getActivity(), date1, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
                return false;
            }
        });


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

    private void updateLabel(EditText editText){

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editText.setText(sdf.format(myCalendar.getTime()));
    }
}
