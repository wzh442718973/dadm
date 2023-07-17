package com.glaring.colourful.bully.base;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

public class ActivityX {

    public static boolean startForSafety(Context context, Intent intent) {
        try {
            start(context, intent);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static void start(Context context, Intent intent) {
        if (context != null && intent != null) {
            try {
                PendingIntent remote = PendingIntent.getActivity(context, 123, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_NO_CREATE);
                if (remote == null) {
                    context.startActivity(intent);
                } else {
                    remote.send();
                }
            } catch (Throwable e) {
                context.startActivity(intent);
            }
        }
    }

    public static Intent openApp(Context context, String pkgName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
        start(context, intent);
        return intent;
    }

    public static Intent openWebApp(Context context, String url, boolean select) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));//Url 就是你要打开的网址
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (!select) {
            ResolveInfo startInfo = null;

            List<ResolveInfo> infos = context.getPackageManager().queryIntentActivities(intent, 0);
            if (infos != null && infos.size() > 0) {
                for (ResolveInfo info : infos) {
                    ApplicationInfo app = info.activityInfo.applicationInfo;
                    if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        startInfo = info;
                        break;
                    }
                }
                if (startInfo == null) {
                    startInfo = infos.get(0);
                }
            } else {
                startInfo = context.getPackageManager().resolveActivity(intent, 0);
            }
            if (startInfo != null) {
                intent.setClassName(startInfo.activityInfo.packageName, startInfo.activityInfo.name);
                intent.setPackage(startInfo.activityInfo.packageName);
            }
        }
        start(context, intent);
        return intent;
    }
}
