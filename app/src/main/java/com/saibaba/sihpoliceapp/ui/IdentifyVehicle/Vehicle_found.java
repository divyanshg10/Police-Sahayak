package com.saibaba.sihpoliceapp.ui.IdentifyVehicle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saibaba.sihpoliceapp.R;

public class Vehicle_found extends AppCompatActivity {

    private static final String TAG = "Vehicle_found";
    private TextView name,age,mobile,registration,engine,chassis;
    private String registrationNumber,sname,sage,smobile,sengine,schasis;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_found);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Checking database");
        progressDialog.show();
        registrationNumber=getIntent().getStringExtra("reg");
        registrationNumber = registrationNumber.replaceAll("\\s", "");
        Log.d(TAG, "onCreate: "+registrationNumber);
        init();

        FirebaseDatabase.getInstance().getReference().child("Vehicle Lost").child(registrationNumber)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            Log.d(TAG, "onDataChange: no found");
                            progressDialog.dismiss();
                            finish();
                        }else{
                            getData(dataSnapshot);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: "+databaseError.getMessage());
                        progressDialog.dismiss();
                        finish();
                    }
                });
    }

    private void getData(DataSnapshot ds){
        sengine=(String)ds.child("vehicle_engineno").getValue();
        schasis=(String)ds.child("vehicle_chasisno").getValue();
        FirebaseDatabase.getInstance().getReference().child("USERS").child((String)ds.child("useruid").getValue())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sname=(String)dataSnapshot.child("Name").getValue();
                        sage=(String)dataSnapshot.child("Age").getValue();
                        smobile=(String)dataSnapshot.child("mobileNo").getValue();
                        progressDialog.dismiss();

                        registration.setText(registrationNumber);
                        chassis.setText(schasis);
                        engine.setText(sengine);
                        name.setText(sname);
                        mobile.setText(smobile);
                        age.setText(sage);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void init(){
        name=findViewById(R.id.vfinfo_name);
        age=findViewById(R.id.vfinfo_age);
        mobile=findViewById(R.id.vfinfo_mobile);
        registration=findViewById(R.id.vfinfo_reg);
        engine=findViewById(R.id.vfinfo_engine);
        chassis=findViewById(R.id.vfinfo_chassis);
    }

}
