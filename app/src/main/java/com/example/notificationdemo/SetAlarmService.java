package com.example.notificationdemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import com.gerli.handsomeboy.gerlisqlitedemo.GerliDatabaseManager;


public class SetAlarmService extends Service {
    final static String TAG = "##SetAlarmService";
    final static int MORNING = 10;
    final static int AFTERNOON = 21;
    final static int NIGHT = 22;
    public final static String ACTION_BROADCAST = "ACTION_MY_BROADCAST";
    final static ArrayList<Integer> alarmHour = new ArrayList<>(Arrays.asList(MORNING, AFTERNOON, NIGHT)) ;
    final static int alarmMinute = 10;
    final static int alarmSecond = 0;
    Thread doInBackground;
    long today, morning, afternoon, midnight;

    GerliDatabaseManager manager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() executed");
        setTimes();
        manager = new GerliDatabaseManager(this);
        if(doInBackground == null){
            doInBackground = new Thread(runnable);
            doInBackground.start();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
        isContinue = false;
        try {
            doInBackground.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    boolean isContinue = true;
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            while (isContinue){
                Calendar cal = Calendar.getInstance();
                // 設定於 1 min 後執行
                cal.add(Calendar.MINUTE, 1);
                int alarmH = cal.get(Calendar.HOUR_OF_DAY);
                int alarmMin = cal.get(Calendar.MINUTE);
                int alarmSec = cal.get(Calendar.SECOND);
                //Log.d(TAG, String.valueOf(alarmH) + ":" +String.valueOf(alarmMin));
                if(alarmHour.contains(alarmH) && alarmMinute == alarmMin && alarmSecond == alarmSec
                        /**/&& !hasDone(cal.getTimeInMillis()) /**/) {
                    Log.d(TAG, "SetAlarm");
                    Intent intent = new Intent(ACTION_BROADCAST);

                    PendingIntent pi = PendingIntent.getBroadcast(SetAlarmService.this, 1, intent, PendingIntent.FLAG_ONE_SHOT);

                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private boolean hasDone(long currentTime){
        boolean done = false;
        int mode = 0;
        long base = 0;
        if(today <= currentTime && currentTime <= morning){
            mode = 1;
            base = today;
        } else if(morning < currentTime && currentTime <= afternoon){
            mode = 2;
            base = morning;
        } else if(afternoon < currentTime && currentTime <= midnight){
            mode = 3;
            base = afternoon;
        }

        long latestTime = manager.getLatestRecordTime().getTime();
        switch (mode){
            case 1:
            case 2:
            case 3:
                if (latestTime - base >= 0)
                    done = true;
                break;
        }
        return done;
    }

    private void setTimes(){
        Calendar calendar = Calendar.getInstance();
        //TODAY in long represent
        calendar.clear(Calendar.MILLISECOND);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.HOUR_OF_DAY);
        today = calendar.getTimeInMillis();
        //TODAY MORNING in long represent
        calendar.set(Calendar.HOUR_OF_DAY,MORNING);
        morning = calendar.getTimeInMillis();
        //TODAY AFTERNOON in long represent
        calendar.set(Calendar.HOUR_OF_DAY,AFTERNOON);
        afternoon = calendar.getTimeInMillis();
        //TODAY NIGHT in long represent
        calendar.set(Calendar.MILLISECOND,999);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        midnight = calendar.getTimeInMillis();
    }
}
