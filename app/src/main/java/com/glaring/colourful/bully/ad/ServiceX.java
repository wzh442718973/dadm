package com.glaring.colourful.bully.ad;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

public abstract class ServiceX {

    public static Notification startForeground(Service service, String channelId, String channelName, String title, int nIcon, String content, PendingIntent intent) {
        Notification notification = null;
        if (intent == null) {
            Intent mainIntent = service.getPackageManager().getLaunchIntentForPackage(service.getPackageName());
            if (mainIntent != null) {
                intent = PendingIntent.getActivity(service.getApplication(), service.hashCode(), mainIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_NO_CREATE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);

            notification = new Notification.Builder(service, channelId)
//                    .setTicker("Nature")
                    .setSmallIcon(nIcon)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setContentIntent(intent)
                    .setFullScreenIntent(intent, false)
                    .build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
        } else if (Build.VERSION.SDK_INT >= 16) {
            notification = new Notification.Builder(service)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(nIcon)
                    .setContentIntent(intent)
                    .setFullScreenIntent(intent, false)
                    .build();
        } else {
            notification = new Notification();
            notification.contentIntent = notification.fullScreenIntent = intent;
        }
        service.startForeground(1, notification);
        return notification;
    }

    public static void start(Context context, Intent intent) {
        if (context != null && intent != null) {
            try {
                PendingIntent remote = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    remote = PendingIntent.getForegroundService(context, 123, intent, PendingIntent.FLAG_NO_CREATE);
                } else {
                    remote = PendingIntent.getService(context, 123, intent, PendingIntent.FLAG_NO_CREATE);
                }

                if (remote != null) {
                    remote.send();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent);
                    } else {
                        context.startService(intent);
                    }
                }
            } catch (Throwable e) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(intent);
                    } else {
                        context.startService(intent);
                    }
                } catch (Throwable e2) {
                    throw e2;
                }
            }
        }
    }
}
