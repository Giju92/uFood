package com.example.ufoodrestaurant.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Luigi on 08/06/2016.
 */
public class G3BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // If the broadcast receiver is enabled it receives the boot event from the system
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Set an alarm that launch the Notification Service every 30 seconds
            Intent iHeartBeatService = new Intent(context, G3NotifyService.class);
            PendingIntent piHeartBeatService = PendingIntent.getService(context, 0, iHeartBeatService, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(piHeartBeatService);
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30000, piHeartBeatService);
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, piHeartBeatService);
            } else if (android.os.Build.VERSION.SDK_INT >= 19) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, piHeartBeatService);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, piHeartBeatService);
            }
        }
    }
}