package com.deadanddeceased.timeloop;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "timeloop:wakeLock");
        wakeLock.acquire();

        String timerName = "Alarm";
        int id = 1;
        if (intent.hasExtra("name") && intent.hasExtra("id")) {
            timerName = intent.getStringExtra("name");
            id = intent.getIntExtra("id", 1);
        }
        Notification.Builder builder =
                new Notification.Builder(context)
                        .setSmallIcon(R.mipmap.timeloop)
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(timerName);

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);

        wakeLock.release();
    }
}