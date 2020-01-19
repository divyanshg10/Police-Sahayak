package com.saibaba.sihpoliceapp.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saibaba.sihpoliceapp.Constants;
import com.saibaba.sihpoliceapp.MainActivity;
import com.saibaba.sihpoliceapp.R;
import com.saibaba.sihpoliceapp.app;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class locationService extends Service {

    static LocationServiceStatus locationServiceStatus=LocationServiceStatus.NOTINITIALISED;
    private static final String TAG = "locationService";
    private Notification notification;
    private DatabaseReference databaseReferenceForLocation,databaseReferenceForSOS;
    private ChildEventListener childEventListener;
    private Location finalLocation;
    private NotificationManagerCompat notificationManagerCompat;
    private int notificationID;
    private long previousSOS;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private HashMap<String,Object> patrolTrack;
    private String district,stationCode,state,uid;
    private String date;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationID=2;
        notificationManagerCompat= NotificationManagerCompat.from(this);
        locationServiceStatus=LocationServiceStatus.INITIALISED;
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,0);
        notification= new NotificationCompat.Builder(this, app.CHANNEL_ID_1)
                .setContentTitle("sharing location")
                .setProgress(0,0,true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setContentText("Your location is being shared")
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                .build();

        Log.d(TAG, "onCreate: created service");
        databaseReferenceForSOS= FirebaseDatabase.getInstance().getReference().child("Emergency");
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        district=stationCode=state=uid="";
    }

    @Override
    public void onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        databaseReferenceForLocation.removeValue();
        locationServiceStatus=LocationServiceStatus.STOPPED;
        patrolTrack.put("end-time",System.currentTimeMillis());
        patrolTrack.put("end-lat",finalLocation.getLatitude());
        patrolTrack.put("end-lon",finalLocation.getLongitude());
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("police-station").child(stationCode).child("patrol").child(date).child(uid).child(String.valueOf(System.currentTimeMillis()));
        databaseReference.setValue(patrolTrack);
        Log.d(TAG, "onDestroy: service destroyed");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1,notification);
        patrolTrack=new HashMap<>();
        date = getCurrentDate();
        locationServiceStatus=LocationServiceStatus.RUNNNING;
        HashMap<String,String> hashMap= Constants.getDataFromSharedPreferences(this);
        try {
            district = hashMap.get(Constants.USER_DISTRICT);
            uid=hashMap.get(Constants.USER_UID);
            state = hashMap.get(Constants.USER_STATE);
            stationCode = hashMap.get(Constants.USER_STATION_ID);
            databaseReferenceForLocation = FirebaseDatabase.getInstance().getReference().child("patrol").child(state).child(district).child(stationCode).child(hashMap.get(Constants.USER_UID));
            startServiceTasks();
            listenForSOSAlerts();
        }catch (Exception e){
            Log.d(TAG, "onStartCommand: "+e.getMessage());
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startServiceTasks(){
        Log.d(TAG, "startServiceTasks: started service");

        //setting location request
        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(500)
                .setInterval(1000)
                .setMaxWaitTime(1500);
        Log.d(TAG, "startServiceTasks: expiration time is "+locationRequest.getExpirationTime());

        //setting location callback
        locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult!=null){
                    finalLocation=locationResult.getLastLocation();
                    updateToDatabase(finalLocation);
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,Looper.getMainLooper());
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    patrolTrack.put("start-time",System.currentTimeMillis());
                    patrolTrack.put("start-lat",location.getLatitude());
                    patrolTrack.put("start-lon",location.getLongitude());
                }
            }
        });
    }

    private void updateToDatabase(Location location){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("latitude",location.getLatitude());
        hashMap.put("longitude",location.getLongitude());
        databaseReferenceForLocation.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: updated location");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed updating location");
            }
        });
    }


    private void listenForSOSAlerts(){
        databaseReferenceForSOS.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    previousSOS = dataSnapshot.getChildrenCount();
                    databaseReferenceForSOS.addChildEventListener(childEventListener);
                }catch (Exception e){
                    Log.e(TAG, "onDataChange: exception is "+e.getMessage() );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(previousSOS>0){
                    Log.d("previous>>>", "onChildAdded: previous sos : "+previousSOS);
                    previousSOS--;
                }else {
                    try {
                        double latitude, longitude;
                        double platitude = 0, plongitude = 0;
                        double distance;
                        if (dataSnapshot.exists()) {
                            latitude = (double) dataSnapshot.child("Latitude").getValue();
                            longitude = (double) dataSnapshot.child("Longitude").getValue();
                            if (finalLocation != null)
                                platitude = finalLocation.getLatitude();
                            plongitude = finalLocation.getLongitude();
                            distance = getDistance(latitude, longitude, platitude, plongitude);
                            Log.d(TAG, "onChildAdded: distance is " + distance);
                            if (distance < 3)
                                createSOSNotification(latitude, longitude, platitude, plongitude);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onChildAdded: exception thrown " + e.getMessage());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

    }

    private void createSOSNotification(double latitude,double longitude,double platitude,double plongitude){
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String directionUri="http://maps.google.com/maps?saddr="+platitude+","+plongitude+"&daddr="+latitude+","+longitude;
        Notification notification=new NotificationCompat.Builder(this,app.CHANNEL_ID_2)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("click to get directions to reach there")
                .setContentTitle("Someone is in trouble nearby")
                .setContentIntent(PendingIntent.getActivity(
                        getApplicationContext(),0,new Intent(Intent.ACTION_VIEW, Uri.parse(directionUri)),0
                ))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setSound(soundUri)
                .build();
        notificationManagerCompat.notify(notificationID++,notification);

    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static LocationServiceStatus getLocationServiceStatus(){
        return locationServiceStatus;
    }

    private String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        return df.format(c);
    }

}
