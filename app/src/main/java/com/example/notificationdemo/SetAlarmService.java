package com.example.notificationdemo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Calendar;


public class SetAlarmService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final int notifyID = 2; // 通知的識別號碼
        NotificationManager notificationManager;
        Notification notification;

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
        // 建立通知
        notification = new Notification.Builder(getApplicationContext()).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("NotificationDemo").setContentText("setAlarm").build();
        notificationManager.notify(notifyID, notification); // 發送通知

        Calendar cal = Calendar.getInstance();
        // 設定於 30 sec 後執行
        cal.add(Calendar.SECOND, 30);

        Intent intent = new Intent(this, PlayReceiver.class);
        intent.putExtra("msg", "my_notification");

        PendingIntent pi = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
    }
}
