package com.glaring.colourful.bully.base;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;

public class AndroidX {

    public static Bitmap fromDrawable(Drawable drawable){
        if(drawable == null){
            return null;
        }else if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }else{
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static PackageInfo getAppInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Throwable e) {
            return null;
        }
    }

    public static Context getBaseContext(Context context) {
        while (context != null) {
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                break;
            }
        }
        return context;
    }

    public static Context getApplication(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof Application) {
            return context;
        } else {
            return context.getApplicationContext();
        }
    }

    public static Intent registerReceiver(Context context, BroadcastReceiver receiver, IntentFilter filter) {
        Intent intent = null;
        try {
            intent = context.registerReceiver(receiver, filter);
        } catch (Throwable e) {
            unregisterReceiver(context, receiver);
            intent = context.registerReceiver(receiver, filter);
        }
        return intent;
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver receive) {
        try {
            context.unregisterReceiver(receive);
        } catch (Throwable e) {

        }
    }

    public static ComponentName[] getNotificationListeners(Context context) {
        final String listeners = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        final List<ComponentName> list = new ArrayList<>();

        if (listeners != null) {
            String[] arrays = listeners.split(":");
            if (arrays != null) {
                for (String item : arrays) {
                    ComponentName cn = ComponentName.unflattenFromString(item);
                    if (cn != null) {
                        list.add(cn);
                    }
                }
            }
        }
        return list.toArray(new ComponentName[0]);
    }

    public static String getProcessName(Context context) {
        final int pid = Process.myPid();
        context = getApplication(context);
        final ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (info.pid == pid) {
                return info.processName;
            }
        }
        return null;
    }

    public static boolean checkActivityAction(Activity activity) {
        if (activity == null || activity.isFinishing()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 17) {
            return !activity.isDestroyed();
        }
        return true;
    }

    public static boolean isMainProcess(Context context) {
        context = getApplication(context);
        final int pig = Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {
            if (info.pid == pig && info.pkgList != null) {
                for (String pkg : info.pkgList) {
                    if (pkg.equals(info.processName)) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    public static int checkSelfPermission(Context context, String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        return context.checkPermission(permission, Process.myPid(), Process.myUid());
    }

    public static void startForegroundService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= 26) {
            context.startForegroundService(intent);
        } else {
            // Pre-O behavior.
            context.startService(intent);
        }
    }

}
