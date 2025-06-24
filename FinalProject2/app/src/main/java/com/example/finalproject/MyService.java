package com.example.finalproject;

import static android.app.PendingIntent.FLAG_MUTABLE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyService extends Service {

    public static final int CHANNEL_ID_INT = 1;
    NotificationManager mNotiMgr;
    Notification.Builder mNotifyBuilder;
    DatabaseReference database;
    boolean dbChanged = false;
    boolean firstTime = true;
    int numberOfUpdate = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        initForeground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent localIntent = new Intent();
                localIntent.setAction("dbUpdated");

                database = FirebaseDatabase.getInstance().getReference("show");
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        dbChanged = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                while(true){

                    if(dbChanged) {
                        dbChanged = false;
                        localIntent.putExtra("foregroundServiceUpdate", 1);
                        sendBroadcast(localIntent);
                        if (firstTime) {
                            firstTime = false;
                            //updateNotification("First Time");
                        } else {
                            updateNotification("There is an update in a Show" + numberOfUpdate);
                            numberOfUpdate++;
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

        return START_NOT_STICKY;
    }


    private void initForeground(){
        String CHANNEL_ID = "my_channel_01";
        if (mNotiMgr==null)
            mNotiMgr= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"My main channel", NotificationManager.IMPORTANCE_DEFAULT);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
// Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_MUTABLE);
        mNotifyBuilder = new Notification.Builder(this, CHANNEL_ID).setContentTitle("Testing Notification...").setSmallIcon(R.drawable.ic_launcher_foreground).setContentIntent(pendingIntent);
        startForeground(CHANNEL_ID_INT, updateNotification(Integer.toString(0)));
    }


    public Notification updateNotification(String details){
        mNotifyBuilder.setContentText(details).setOnlyAlertOnce(false);
        Notification noti = mNotifyBuilder.build();
        noti.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        mNotiMgr.notify(CHANNEL_ID_INT, noti);
        return noti;

    }
}
