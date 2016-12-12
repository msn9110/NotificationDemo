package com.example.notificationdemo;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;


public class EventReceiver extends BroadcastReceiver {

    final static String TAG = "##EventRceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            //we double check here for only boot complete event
            case Intent.ACTION_BOOT_COMPLETED:
                Log.d(TAG, "BOOT COMPLETE");
                //here we start the service

                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                boolean needAlarm = sharedPrefs.getBoolean("notice",false);
                if(needAlarm){
                    Intent serviceIntent = new Intent(context, SetAlarmService.class);
                    context.startService(serviceIntent);
                }
                break;
            case SetAlarmService.ACTION_BROADCAST:
                final int notifyID = 3;
                final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                // 建立通知
                final Notification notification = new Notification.Builder(context.getApplicationContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("NotificationDemo")
                        .setContentText("該記帳囉").build();
                notificationManager.notify(notifyID, notification); // 發送通知
                break;
        }
    }
}
