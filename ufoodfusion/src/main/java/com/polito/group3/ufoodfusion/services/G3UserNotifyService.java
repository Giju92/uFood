package com.polito.group3.ufoodfusion.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.ufoodlibrary.firebase.NetworkDownException;
import com.example.ufoodlibrary.firebase.NotAuthenticatedException;
import com.example.ufoodlibrary.objects.G3OrderObj;
import com.example.ufoodlibrary.utilities.G3Application;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.polito.group3.ufoodfusion.R;
import com.polito.group3.ufoodfusion.activities.G3UserOrdersActivity;
import com.polito.group3.ufoodfusion.activities.G3UserSearchRestaurant;


import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alfonso-LAPTOP on 08/06/2016.
 */
public class G3UserNotifyService extends Service {

    private NotifyOrder notifyOrder;
    private long INIT_SLEEP = 5000;
    private long TIMEOUT = 15000;
    private String accountId;

    @Override
    public void onCreate() {
        this.accountId = G3Application.fManager.getCurrentId();
        notifyOrder = new NotifyOrder();
        Thread t = new Thread(notifyOrder);
        t.run();
        Log.d("Notify Service", "Thread started");
    }







    private class NotifyOrder extends Thread {


        @Override
        public void run() {


            Timer timer = new Timer();
            TimerTask timertask = new TimerTask() {
                @Override
                public void run() {
                    verify_orders();
                }
            };

            timer.schedule(timertask, INIT_SLEEP, TIMEOUT);


        }

        private void verify_orders() {

            if(G3Application.fManager.isSignedin() && G3Application.isNetworkAvailable()){

                try {
                    G3Application.fManager.getUserOrders(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                G3OrderObj order = data.getValue(G3OrderObj.class);
                                if (!order.isUserVisualized()) {
                                    sendNotification(order.getOrderState());
                                    order.setUserVisualized(true);

                                    try {
                                        G3Application.fManager.setUserOrderVisualized(order.getOrderid());
                                    } catch (NetworkDownException e) {

                                    }
                                    break;
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } catch (NotAuthenticatedException | NetworkDownException e) {

                }

                try {
                    G3Application.fManager.getNotifyDaily(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for(DataSnapshot d: dataSnapshot.getChildren()){
                                Boolean vv = (Boolean) d.getValue();
                                if(vv == true){
                                    sendDailyNotification(d.getKey());
                                    try {
                                        G3Application.fManager.setNotifyDaily(d.getKey(),false);
                                    } catch (NetworkDownException | NotAuthenticatedException e) {}
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } catch (NotAuthenticatedException | NetworkDownException e) {

                }

            }

        }



    }


    private void sendDailyNotification(String nameRestaurant) {

        NotificationCompat.Builder mBuilder= null;


        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.delivery_white)
                .setContentTitle(getResources().getString(R.string.daily_menu))
                .setContentText(nameRestaurant+ ": " +getResources().getString(R.string.dailynotification));


        if(mBuilder != null){
            mBuilder.setAutoCancel(true);

            Intent resultIntent = new Intent(this, G3UserSearchRestaurant.class);

            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            resultIntent.putExtra("orders", true);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);

            PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // mId allows you to update the notification later on.
            mNotificationManager.notify(11, mBuilder.build());
        }

    }

    private void sendNotification(int orderState) {
        NotificationCompat.Builder mBuilder= null;
        if(orderState == 1){
             mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.delivery_white)
                            .setContentTitle(getResources().getString(R.string.order_evaded))
                            .setContentText(getResources().getString(R.string.order_evaded_mex));
        }
        if(orderState == 2){
            mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.delivery_white)
                            .setContentTitle(getResources().getString(R.string.order_deleted))
                            .setContentText(getResources().getString(R.string.order_deleted_mex));
        }

        if(mBuilder != null){
            mBuilder.setAutoCancel(true);

            Intent resultIntent = new Intent(this, G3UserOrdersActivity.class);

            resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            resultIntent.putExtra("orders", true);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);

            PendingIntent contentIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // mId allows you to update the notification later on.
            mNotificationManager.notify(10, mBuilder.build());
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
