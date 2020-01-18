package com.saibaba.sihpoliceapp.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saibaba.sihpoliceapp.Constants;
import com.saibaba.sihpoliceapp.R;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MapsActivity";
    private ChildEventListener childEventListener;
    private DatabaseReference databaseReference[];
    private DatabaseReference baseReference;
    private HashMap<String, Marker> hashMap;
    private  FirebaseDatabase firebaseDatabase;
    private HashMap<String,String> userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the hashMap is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hashMap=new HashMap<>();
        userData= Constants.getDataFromSharedPreferences(this);
        firebaseDatabase=FirebaseDatabase.getInstance();
        baseReference= firebaseDatabase.getReference().child("patrol");
    }


    /**
     * Manipulates the hashMap once available.
     * This callback is triggered when the hashMap is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: hashMap ready");
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Patna = new LatLng(25.5941, 85.1376);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Patna,10));
        init();
        setDatabaseReferences();
    }

    private void init(){

        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Log.d(TAG, "onChildAdded: "+dataSnapshot);
                    Log.d(TAG, "onChildAdded: added "+getLatLng(dataSnapshot));
                    String key=dataSnapshot.getKey();
                    Marker marker=mMap.addMarker(new MarkerOptions()
                            .position(getLatLng(dataSnapshot))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                            .title(key));
                    hashMap.put(key,marker);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged: child changed");
                if(dataSnapshot.exists()) {
                    String key=dataSnapshot.getKey();
                    Marker marker=hashMap.get(key);
                    marker.setPosition(getLatLng(dataSnapshot));
                    hashMap.put(key,marker);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String key=dataSnapshot.getKey();
                    hashMap.get(key).remove();
                    hashMap.remove(key);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: error in child listener"+databaseError.getMessage() );
            }
        };
    }

    private LatLng getLatLng(DataSnapshot dataSnapshot){
        return new LatLng((double)dataSnapshot.child("latitude").getValue(),(double)dataSnapshot.child("longitude").getValue());
    }

    private void setDatabaseReferences(){
        Log.d(TAG, "setDatabaseReferences: called");
        final String state=userData.get(Constants.USER_STATE);
        final String district=userData.get(Constants.USER_DISTRICT);
        if(userData.get(Constants.USER_LEVEL).equals("2")){
            databaseReference=new DatabaseReference[1];
            databaseReference[0]=baseReference.child(state).child(district).child(userData.get(Constants.USER_STATION_ID));
            addListeners();
        }else if(userData.get(Constants.USER_LEVEL).equals("1")){
            final DatabaseReference databaseReferenceState=firebaseDatabase.getReference().child("places").child(Constants.USER_STATE).child(state).child(district);
            databaseReferenceState.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        int childs=(int)dataSnapshot.getChildrenCount();
                        databaseReference=new DatabaseReference[childs];
                        int index=0;
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            databaseReference[index]=baseReference.child(state).child(district).child(childSnapshot.getKey());
                            index++;
                        }
                    }
                    addListeners();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled: "+databaseError.getMessage() );
                }
            });
        }
    }

    private void addListeners(){
        Log.d(TAG, "addListeners: called");
        if(databaseReference!=null)
            for(int i=0;i<databaseReference.length;i++){
                databaseReference[i].addChildEventListener(childEventListener);
            }
    }

    private void removeListeners(){
        if(databaseReference!=null)
            for(int i=0;i<databaseReference.length;i++){
                databaseReference[i].removeEventListener(childEventListener);
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
        if(mMap!=null){
            addListeners();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeListeners();
        Iterator iterator=hashMap.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry entry=(Map.Entry)iterator.next();
            ((Marker)entry.getValue()).remove();
        }
        hashMap=new HashMap<>();
    }
}
