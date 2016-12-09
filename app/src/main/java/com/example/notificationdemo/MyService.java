package com.example.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;


public class MyService extends Service {

    final int notifyID = 1; // 通知的識別號碼
    NotificationManager notificationManager;
    Notification notification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //dosomething when service create
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
        // 建立通知
        notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("NotificationDemo").setContentText("PlayAlarm").build();
        notificationManager.notify(notifyID, notification); // 發送通知
    }
    
}
