package com.advantal.onebrush.utilies_pkg;

import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;


public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN", s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("Notificationdata", "" + remoteMessage.getData());
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why thisAppc may be: https://goo.gl/39bRNJLog.d(TAG, "From: " + remoteMessage.getFrom());
        //https://pushtry.com/
        Map data = remoteMessage.getData();



    }


}


