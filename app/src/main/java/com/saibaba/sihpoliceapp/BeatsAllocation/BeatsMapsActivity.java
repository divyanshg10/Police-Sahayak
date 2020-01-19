package com.saibaba.sihpoliceapp.BeatsAllocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Application;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.saibaba.sihpoliceapp.Constants;
import com.saibaba.sihpoliceapp.Modal.BeatDetail;
import com.saibaba.sihpoliceapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BeatsMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "BeatsMapsActivity";

    private GoogleMap mMap;
    private String uid,station;
    private FloatingActionButton floatingActionButton;
    private HashMap<String,String> hashMap;
    private static HashMap<String, Marker> markersHashMap;
    private LatLng Patna;
    private DatabaseReference baseDatabaseReference;
    private ArrayList<BeatDetail> beatArrayList;
    long childCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beats_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        uid = station = "";
        hashMap= Constants.getDataFromSharedPreferences(this);
        uid=hashMap.get(Constants.USER_UID);
        station=hashMap.get(Constants.USER_STATION_ID);
        baseDatabaseReference=FirebaseDatabase.getInstance().getReference();
        markersHashMap=new HashMap<>();
        beatArrayList=new ArrayList<>();
        getDataFromDataBase();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        Patna = new LatLng(25.5941, 85.1376);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Patna,15));

        getDataOfAlerts();

        FirebaseDatabase.getInstance().getReference().child("police-station").child(station).child("sub-ins")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            setMarkers(dataSnapshot);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled: "+databaseError.getMessage() );
                    }
                });
        floatingActionButton=findViewById(R.id.fabBeat);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                markersHashMap.put(marker.getTitle(),marker);
            }
        });
    }

    private void setMarkers(DataSnapshot ds){
        double inc= 0.001d;
        for(DataSnapshot dataSnapshot:ds.getChildren()){
            MarkerOptions markerOptions=new MarkerOptions()
                    .title((String)dataSnapshot.getKey())
                    .draggable(true)
                    .position(new LatLng(Patna.latitude+inc,Patna.longitude));
            markerOptions.snippet("name : "+dataSnapshot.getValue());
            Marker marker=mMap.addMarker(markerOptions);
            markersHashMap.put(dataSnapshot.getKey(),marker);
            inc+=0.001d;
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAllotedSpaces();
                finish();
            }
        });
    }

    private void getDataOfAlerts(){
        FirebaseDatabase.getInstance().getReference().child("crime-alert").child("Bihar").child("Patna")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                CircleOptions circleOptions=new CircleOptions();
                                circleOptions.radius(1000);
                                circleOptions.center(new LatLng((double)dataSnapshot1.child("latitude").getValue(),(double)dataSnapshot1.child("longitude").getValue()));
                                circleOptions.strokeWidth(0);
                                long crimes=(long)dataSnapshot1.child("alerts").getValue();
                                if(crimes<200){
                                    circleOptions.fillColor(0x50ffc107);
                                }else if(crimes<400){
                                    circleOptions.fillColor(0x50FF5722);
                                }else{
                                    circleOptions.fillColor(0x50ff0000);
                                }
                                mMap.addCircle(circleOptions);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void saveAllotedSpaces(){
        Iterator iterator=markersHashMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            Marker marker=(Marker)entry.getValue();
            HashMap<String,Object> map=new HashMap<>();
            map.put("latitude",marker.getPosition().latitude);
            map.put("longitude",marker.getPosition().longitude);
            FirebaseDatabase.getInstance().getReference().child("police").child((String)entry.getKey()).child("allocation").setValue(map);
        }
    }

    private void getDataFromDataBase(){
        baseDatabaseReference.child("police-station").child(station).child("sub-ins")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            childCount=dataSnapshot.getChildrenCount();
                           for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                               getProfileData(dataSnapshot1.getKey());
                           }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled: exception occurred in getDataFromDatabase "+databaseError.getMessage() );
                    }
                });
    }

    private void getProfileData(String subUid){
        baseDatabaseReference.child("police").child(subUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            final BeatDetail beatDetail=new BeatDetail();
                            beatDetail.setUserImg(dataSnapshot.child("image").child("url").getValue().toString());
                            beatDetail.setLatitude((double)dataSnapshot.child("allocation").child("latitude").getValue());
                            beatDetail.setLongitude((double)dataSnapshot.child("allocation").child("longitude").getValue());
                            beatDetail.setMobile(dataSnapshot.child("phone").getValue().toString());
                            beatDetail.setName(dataSnapshot.child("name").getValue().toString());
                            beatDetail.setUid(dataSnapshot.getKey());
                            beatArrayList.add(beatDetail);
                            childCount--;
                            markersHashMap.get(beatDetail.getUid()).setPosition(new LatLng(beatDetail.getLatitude(),beatDetail.getLongitude()));
                            if(childCount==0){
                                BeatInfoWindowAdapter beatInfoWindowAdapter=new BeatInfoWindowAdapter(beatArrayList,BeatsMapsActivity.this);
                                mMap.setInfoWindowAdapter(beatInfoWindowAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "onCancelled: exception occurred in getProfileData "+databaseError.getMessage());
                    }
                });
    }

}
