package com.saibaba.sihpoliceapp.services;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.saibaba.sihpoliceapp.R;
import com.saibaba.sihpoliceapp.app;

public class NotificationService extends FirebaseMessagingService {

    private static final String TAG = "NotificationService";

    public NotificationService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: message received");
        super.onMessageReceived(remoteMessage);
        Notification notification=new NotificationCompat
                .Builder(this, app.CHANNEL_ID_2)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .build();
        NotificationManagerCompat.from(this).notify(123,notification);
        
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(@NonNull String s, @NonNull Exception e) {
        super.onSendError(s, e);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d(TAG, "onNewToken: new Token created : "+s);
        super.onNewToken(s);
    }
}
