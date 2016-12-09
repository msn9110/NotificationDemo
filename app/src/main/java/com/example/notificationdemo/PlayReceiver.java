package com.example.notificationdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class PlayReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bData = intent.getExtras();
        if(bData.get("msg").equals("my_notification"))
        {
            //要執行的工作
            //Intent intent1 = new Intent(context , MainActivity.class);
            //intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(intent1);
            //執行一個Activity

            Intent intent2 = new Intent(context , MyService.class);
            context.startService(intent2);
            //執行一個Service
        }
    }
}
