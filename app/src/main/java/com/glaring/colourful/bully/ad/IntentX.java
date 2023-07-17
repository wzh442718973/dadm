package com.glaring.colourful.bully.ad;

import static com.glaring.colourful.bully.ad.AndroidX.checkSelfPermission;
import static com.glaring.colourful.bully.ad.AndroidX.getApplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public final class IntentX {
    static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    static final String ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    static final String INSTALL_SHORTCUT_PERMISSION = "com.android.launcher.permission.INSTALL_SHORTCUT";


    /**
     * 打开通知栏监听管理配置界面
     */
    public static final Intent INTENT_NOTIFICATION_LISTENER_SETTINGS = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");

    public static boolean isRequestPinShortcutSupported(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            return context.getSystemService(ShortcutManager.class).isRequestPinShortcutSupported();
        }

        if (checkSelfPermission(context, INSTALL_SHORTCUT_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        for (ResolveInfo info : context.getPackageManager().queryBroadcastReceivers(
                new Intent(ACTION_INSTALL_SHORTCUT), 0)) {
            String permission = info.activityInfo.permission;
            if (TextUtils.isEmpty(permission) || INSTALL_SHORTCUT_PERMISSION.equals(permission)) {
                return true;
            }
        }
        return false;
    }


    private static Object getIcon(Context context, Object icon) {
        final PackageManager pm = getApplication(context).getPackageManager();
        if (icon == null) {
            icon = pm.getApplicationIcon(context.getApplicationInfo());
        }
        Object iconRes = icon;
        if (icon instanceof Bitmap) {
            iconRes = icon;
        } else if (icon instanceof BitmapDrawable) {
            iconRes = ((BitmapDrawable) icon).getBitmap();
        }
        if (Build.VERSION.SDK_INT >= 26) {
            if (iconRes instanceof Integer) {
                return Icon.createWithResource(context, ((Integer) iconRes).intValue());
            } else if (iconRes instanceof Bitmap) {
                return Icon.createWithBitmap((Bitmap) iconRes);
            }
        } else {
            if (iconRes instanceof Integer) {
                return Intent.ShortcutIconResource.fromContext(context, ((Integer) icon).intValue());
            } else if (iconRes instanceof Bitmap) {
                return iconRes;
            }
        }
        return null;
    }

    private static CharSequence getTitle(Context context, Object title) {
        final PackageManager pm = getApplication(context).getPackageManager();
        if (title == null) {
            title = pm.getApplicationLabel(context.getApplicationInfo()).toString();
        }
        String nameRes;
        if (title instanceof Integer) {
            nameRes = context.getString(((Integer) title).intValue());
        } else if (title instanceof CharSequence) {
            nameRes = title.toString();
        } else {
            nameRes = title.toString();
        }
        return nameRes;
    }

    @SuppressLint("NewApi")
    public static ShortcutInfo toShortcutInfo(Context context, String id, Object title, Object icon, Intent intent) {
        ShortcutInfo.Builder builder = new ShortcutInfo.Builder(context, id);
        CharSequence lable = getTitle(context, title);
        builder.setShortLabel(lable);
        builder.setIcon((Icon) getIcon(context, icon));
        builder.setIntent(intent);
        builder.setLongLabel(lable);
        builder.setDisabledMessage(lable);
        builder.setActivity(intent.getComponent());

        return builder.build();
    }


    /**
     * @param context
     * @param title
     * @param icon
     * @param intent  <uses-permission android:name="com.google.android.apps.nexuslauncher.permission.READ_SETTINGS"/>
     *                <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.android.launcher2.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.android.launcher3.permission.READ_SETTINGS" />
     *                <!-- 添加快捷方式 -->
     *                <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
     *                <!-- 移除快捷方式 -->
     *                <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
     *                <uses-permission android:name="org.adw.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.htc.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.qihoo360.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.lge.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="net.qihoo.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="org.adwfreak.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="org.adw.launcher_donut.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.huawei.launcher3.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.fede.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.sec.android.app.twlauncher.settings.READ_SETTINGS" />
     *                <uses-permission android:name="com.anddoes.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.tencent.qqlauncher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.huawei.launcher2.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.android.mylauncher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.ebproductions.android.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.oppo.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.lenovo.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.huawei.android.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="com.bbk.launcher2.permission.READ_SETTINGS" />
     *                <uses-permission android:name="cn.nubia.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="cn.nubia.launcher.permission.WRITE_SETTINGS" />
     *                <uses-permission android:name="cn.nubia.launcher2.permission.READ_SETTINGS" />
     *                <uses-permission android:name="cn.nubia.launcher2.permission.WRITE_SETTINGS" />
     *                <uses-permission android:name="net.oneplus.launcher.permission.READ_SETTINGS" />
     *                <uses-permission android:name="net.oneplus.launcher.permission.WRITE_SETTINGS" />
     *                <!-- 高版本 -->
     *                <uses-permission android:name="android.permission.INSTALL_SHORTCUT" />
     *                <uses-permission android:name="android.permission.UNINSTALL_SHORTCUT" />
     */
    public static boolean createShortcut(Context context, String id, Object title, Object icon, final Intent intent, final IntentSender callback) {
        if (intent.getAction() == null) {
            intent.setAction(Intent.ACTION_VIEW);
        }

        // 给桌面发送一个广播
        if (Build.VERSION.SDK_INT >= 26) {
            return context.getSystemService(ShortcutManager.class).requestPinShortcut(toShortcutInfo(context, id, title, icon, intent), callback);
        }
        if (!isRequestPinShortcutSupported(context)) {
            return false;
        }

        final Intent shortcut = new Intent(ACTION_INSTALL_SHORTCUT);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);        // 设置快捷方式执行的操作
        shortcut.putExtra("duplicate", false);            //不允许重复创建
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getTitle(context, title));         //开解图标名称
        Object iconRes = getIcon(context, icon);
        if (iconRes == null) {

        } else if (iconRes instanceof Intent.ShortcutIconResource) {
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, (Intent.ShortcutIconResource) iconRes);//快捷方式的图标
        } else if (iconRes instanceof Bitmap) {
            shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON, (Bitmap) iconRes);
        }
        // If the callback is null, just send the broadcast
//        if (callback == null) {
//            context.sendBroadcast(intent);
//            return true;
//        }
        context.sendOrderedBroadcast(shortcut, null, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    Log.e("wzh", "创建成功");
                    if(callback != null){
                        callback.sendIntent(context, 0, null, null, null);
                    }
                } catch (IntentSender.SendIntentException e) {
                    // Ignore
                }
            }
        }, null, Activity.RESULT_OK, null, null);
        return true;
    }

    public static void deleteeShortcut(Context context, String id, Object title, final Intent intent, final IntentSender callback) {
        if (intent.getAction() == null) {
            intent.setAction(Intent.ACTION_VIEW);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            ArrayList<String> ids = new ArrayList<>();
            ids.add(id);
            context.getSystemService(ShortcutManager.class).removeDynamicShortcuts(ids);
            return;
        }

        Intent shortcut = new Intent(ACTION_UNINSTALL_SHORTCUT);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        shortcut.putExtra("duplicate", true);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getTitle(context, title));

        if (callback == null) {
            context.sendBroadcast(shortcut);
            return;
        }
        context.sendOrderedBroadcast(shortcut, null, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    Log.e("wzh", "删除成功");
                    callback.sendIntent(context, 0, null, null, null);
                } catch (IntentSender.SendIntentException e) {
                    // Ignore
                }
            }
        }, null, Activity.RESULT_OK, null, null);
    }

    public static boolean existShortcut(Context context, String title, Intent intent) {


        return false;
//        final PackageManager pm = AndroidX.getApplication(context).getPackageManager();
//
//        boolean result = false;
//        try {
//            final ContentResolver cr = context.getContentResolver();
//            StringBuilder uriStr = new StringBuilder();
//            String authority = LauncherUtil.getAuthorityFromPermissionDefault(context);
//            if (authority == null || authority.trim().equals("")) {
//                authority = LauncherUtil.getAuthorityFromPermission(context, LauncherUtil.getCurrentLauncherPackageName(context) + ".permission.READ_SETTINGS");
//            }
//            uriStr.append("content://");
//            if (TextUtils.isEmpty(authority)) {
//                int sdkInt = android.os.Build.VERSION.SDK_INT;
//                if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
//                    uriStr.append("com.android.launcher.settings");
//                } else if (sdkInt < 19) {// Android 4.4以下
//                    uriStr.append("com.android.launcher2.settings");
//                } else {// 4.4以及以上
//                    uriStr.append("com.android.launcher3.settings");
//                }
//            } else {
//                uriStr.append(authority);
//            }
//            uriStr.append("/favorites?notify=true");
//            Uri uri = Uri.parse(uriStr.toString());
//            Cursor c = cr.query(uri, new String[]{"title", "intent"},
//                    "title=?  and intent=?",
//                    new String[]{title, intent.toUri(0)}, null);
//            if (c != null && c.getCount() > 0) {
//                result = true;
//            }
//            if (c != null && !c.isClosed()) {
//                c.close();
//            }
//        } catch (Exception ex) {
//            result = false;
//            ex.printStackTrace();
//        }
//        return result;
    }

}
