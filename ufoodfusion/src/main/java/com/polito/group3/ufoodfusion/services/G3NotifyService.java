package com.polito.group3.ufoodfusion.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.utilities.G3Application;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.R;
import com.polito.group3.ufoodfusion.activities.G3TabsActivity;
import com.polito.group3.ufoodfusion.fragments.G3OrderFragment;

/**
 * Created by Alfonso-LAPTOP on 02/06/2016.
 */
public class G3NotifyService extends Service{

    private Handler mHandler;
    private long lastTimestamp;
    private int unreadNotifications;

    @Override
    public void onCreate() {

        final SharedPreferences prefs = getSharedPreferences(G3Application.SHARED_PREFERENCE, Context.MODE_PRIVATE);
        unreadNotifications = prefs.getInt(G3Application.UNREAD_NOTIFICATION, 0);
        lastTimestamp = prefs.getLong(G3Application.LAST_TIMESTAMP, System.currentTimeMillis());

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {

                    G3Application.fManager.getNewOrders(lastTimestamp, new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            long currentTimeStamp = System.currentTimeMillis();
                            final SharedPreferences prefs = getSharedPreferences(G3Application.SHARED_PREFERENCE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putLong(G3Application.LAST_TIMESTAMP, currentTimeStamp);

                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                String orderRestId = data.child("restaurantId").getValue(String.class);
                                if (orderRestId.equals(G3Application.fManager.getCurrentId())) {
                                    unreadNotifications++;
                                }

                            }

                            if (unreadNotifications > prefs.getInt(G3Application.UNREAD_NOTIFICATION, 0)) {
                                sendNotification();
                                editor.putInt(G3Application.UNREAD_NOTIFICATION, unreadNotifications);
                            }

                            editor.commit();
//                            G3NotifyService.this.stopSelf();
                            scheduleNextStartAndStopSelf();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("Notify Service", databaseError.getMessage());
//                            G3NotifyService.this.stopSelf();
                            scheduleNextStartAndStopSelf();
                        }
                    });

                } catch (NotAuthenticatedException | NetworkDownException e) {
                    Log.d("Notify Service", e.getMessage());
//                    G3NotifyService.this.stopSelf();
                    scheduleNextStartAndStopSelf();
                }
            }

        }, 1000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private void scheduleNextStartAndStopSelf() {

        // Set an alarm that launch the Notification Service every 30 seconds
        Intent iHeartBeatService = new Intent(getApplicationContext(), G3NotifyService.class);
        PendingIntent piHeartBeatService = PendingIntent.getService(getApplicationContext(), 0, iHeartBeatService, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        mAlarmManager.cancel(piHeartBeatService);
//        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 30000, piHeartBeatService);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            mAlarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 15000, piHeartBeatService);
        } else if (android.os.Build.VERSION.SDK_INT >= 19) {
            mAlarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 15000, piHeartBeatService);
        } else {
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 15000, piHeartBeatService);
        }

        G3NotifyService.this.stopSelf();

    }

    private void sendNotification() {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.delivery_white)
                        .setContentTitle(getResources().getString(R.string.new_order))
                        .setContentText(getResources().getString(R.string.new_order_turor))
                        .setNumber(unreadNotifications);

        mBuilder.setAutoCancel(true);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, G3TabsActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        resultIntent.putExtra("orders", true);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//
//        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(G3TabsActivity.class);
//
//        // Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
        PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (G3OrderFragment.visible) {
            Intent intent = new Intent("RELOAD_DATA");
            sendBroadcast(intent);
        } else {
            G3OrderFragment.refreshNeeded = true;
        }

        // Id allows you to update the notification later on.
        mNotificationManager.notify(10, mBuilder.build());

    }

}
