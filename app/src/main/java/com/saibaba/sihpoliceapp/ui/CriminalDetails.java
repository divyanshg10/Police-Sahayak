package com.saibaba.sihpoliceapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.saibaba.sihpoliceapp.Modal.Face;
import com.saibaba.sihpoliceapp.R;
import com.squareup.picasso.Picasso;

public class CriminalDetails extends AppCompatActivity {

    private static final String TAG = "CriminalDetails";

    private ImageView criminalCaptured,criminalOrg;
    private EditText name,fname,age,crimeDetails,operationalArea;
    private Face face;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criminal_details);
        face=(Face)getIntent().getSerializableExtra("face");
        init();
        getData();
    }

    private void init(){
        criminalCaptured=findViewById(R.id.cimage);
        criminalOrg=findViewById(R.id.c1image);
        name=findViewById(R.id.fcname);
        fname=findViewById(R.id.fcfname);
        age=findViewById(R.id.fcage);
        crimeDetails=findViewById(R.id.fcdetails);
        operationalArea=findViewById(R.id.fctype);
    }

    private void getData(){
        FirebaseDatabase.getInstance().getReference().child("criminals").child(face.getPersonID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        populateFields(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: exception occurred with "+databaseError.getMessage());
                    }
                });
    }

    private void populateFields(DataSnapshot ds){
        byte[] criminalImage = face.getImage();
        criminalCaptured.setImageBitmap(BitmapFactory.decodeByteArray(criminalImage,0,criminalImage.length));
        name.setText((String)ds.child("name").getValue());
        age.setText(getDOB((long)ds.child("dob").getValue()));
        operationalArea.setText((String)ds.child("city").getValue()+" , "+(String)ds.child("district").getValue());
        crimeDetails.setText(getDetails(ds.child("crimes")));
        downloadImage(ds.child("image"));
    }

    private String getDOB(long dob){
        long age=System.currentTimeMillis()-dob;
        age=age/(1000*60*60*24*365);
        return String.valueOf(age);
    }

    private String  getDetails(DataSnapshot ds){
        for(DataSnapshot dataSnapshot:ds.getChildren()){
            return (String)dataSnapshot.child("desc").getValue();
        }
        return "";
    }

    private void downloadImage(DataSnapshot ds){
        for(DataSnapshot dataSnapshot:ds.getChildren()){
            setImage((String)dataSnapshot.child("url").getValue());
            return;
        }
    }

    private void setImage(String downloadURL){
        FirebaseStorage.getInstance().getReference().child(downloadURL)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess: "+uri);
            }
        });
    }

}
