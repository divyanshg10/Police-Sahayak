package com.saibaba.sihpoliceapp;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class app extends Application {
    private static final String TAG = "app";
    public static final String  CHANNEL_ID_1="channel1";
    public static final String  CHANNEL_ID_2="channel2";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            Log.d(TAG, "createNotificationChannel: started creating channels");
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID_1,"channel for foreground service",NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.RED);
            NotificationManager notificationManager=getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel1=new NotificationChannel(CHANNEL_ID_2,"channel for sos messages",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel1.setShowBadge(true);
            notificationChannel1.enableLights(true);
            notificationChannel1.enableVibration(true);
            notificationChannel1.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            notificationChannel.setSound(soundUri,null);
            long vibrationPattern[]={500,500,500,500,500,500,500,500,500};
            notificationChannel1.setVibrationPattern(vibrationPattern);
            notificationManager.createNotificationChannel(notificationChannel1);
        }
    }
}
