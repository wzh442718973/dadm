package com.glaring.colourful.bully.fs.lib;

import static com.glaring.colourful.bully.fs.lib.FusionPack.A;

import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.ad.AndroidX;
import com.glaring.colourful.bully.ad.XObject;
import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.ServiceInterface;
import com.glaring.colourful.bully.mo.RefClass;
import com.glaring.colourful.bully.mo.RefField;

public class NotificationObserver extends IInterfaceObserver {

    final static RefClass TOAST_TN = RefClass.Get("android.widget.Toast$TN");
    final static RefField mPackageName = TOAST_TN.getField("mPackageName");

    public static int index(Object[] args, Class clszz, int start) {
        final int num = args == null ? 0 : args.length;
        for (int i = start; i < num; ++i) {
            if (args[i] != null && clszz.isInstance(args[i])) {
                return i;
            }
        }
        return -1;
    }

    @ServiceInterface("enqueueToast")
    protected Object _enqueueToast(Object source, Method method, Object[] args) throws Throwable {
        int idx = index(args, String.class, 0);
        FusionPack pack = AAAHelper.findPack((String) args[idx], false);
        if (pack != null) {
            args[idx] = FusionPack.A.getPkgName();
            idx = index(args, TOAST_TN.get(), idx + 1);
            if (idx >= 0) {
                mPackageName.set(args[idx], FusionPack.A.getPkgName());
            }
        }
        return super.onInvoke(source, method, args);
    }

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, String.class, 0);
        while (idx >= 0 && idx < 3) {
            FusionPack pack = AAAHelper.findPack((String) args[idx], false);
            if (pack != null) {
                args[idx] = FusionPack.A.getPkgName();
            }
            idx = XObject.index(args, String.class, idx + 1);
        }

        idx = XObject.index(args, Notification.class);
        if (idx >= 0) {
            Notification notif = (Notification) args[idx];
            changeNotification(null, notif);
        }

        return super.onInvoke(source, method, args);
    }


    private static final RefField mSmallIcon = RefField.Get(Notification.class, "mSmallIcon");
    private static final RefField mLargeIcon = RefField.Get(Notification.class, "mLargeIcon");
    private static final RefField mIcon = RefField.Get(Notification.Action.class, "mIcon");

    private static final boolean resetIcon(Context context, Object obj, RefField iconField) {
        Icon icon = iconField.get(obj, null);
        if (icon != null && Icon.TYPE_RESOURCE == icon.getType()) {
            Drawable drawable = icon.loadDrawable(context);
            iconField.set(obj, Icon.createWithBitmap(AndroidX.fromDrawable(drawable)));
            return true;
        }
        return false;
    }

    public static void changeNotification(FusionPack pack, Notification notification) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ApplicationInfo info = notification.extras.getParcelable("android.appInfo");
            if (info != null) {
                info.packageName = A.getPkgName();
                if (pack == null) {
                    pack = AAAHelper.findPack(info.packageName, false);
                }
            }
        }
        if (pack != null) {
            if (resetIcon(pack.mContext, notification, mSmallIcon)) {
                notification.icon = 0;
            }
            if (resetIcon(pack.mContext, notification, mLargeIcon)) {
                notification.icon = 0;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && notification.actions != null) {
                for (Notification.Action action : notification.actions) {
                    resetIcon(pack.mContext, action, mIcon);
                }
            }
        } else {
            Log.e(AdKey.TAG, "Notification not set pack!!!");
        }
    }
}
