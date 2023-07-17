package com.glaring.colourful.bully.fs.lib;

import static com.glaring.colourful.bully.fs.lib.Android.mH;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.glaring.colourful.bully.fs.ak.AdSelectorImpl;
import com.glaring.colourful.bully.fs.lib.gms.C2dmREGISTER;
import com.glaring.colourful.bully.fs.lib.gms.GetInstallReferrerService;
import com.glaring.colourful.bully.fs.lib.gms.GmsServiceBroker;
import com.glaring.colourful.bully.fs.lib.gms.InAppBillingService;
import com.glaring.colourful.bully.ad.sdk;
import com.glaring.colourful.bully.mo.ProxyManager;
import com.glaring.colourful.bully.mo.ServiceManager;
import com.glaring.colourful.bully.mo.RefField;

public final class AAAHelper {
    static {
        sdk.fun(1);
    }
    static final Handler handle = new Handler(Looper.getMainLooper());
    static final FusionPack[] mPacks = FusionPack.values();
    static final int PACK_SIZE = mPacks.length;

    public static FusionPack findPack(String pkgName, boolean host) {
        if (pkgName != null) {
            for (int i = (host ? 0 : 1); i < PACK_SIZE; ++i) {
                if (pkgName.equals(mPacks[i].getPkgName())) {
                    return mPacks[i];
                }
            }
        }

        return null;
    }

    static String[] getAllPkgs() {
        String[] pkgs = new String[PACK_SIZE];
        int idx = 0;
        for (int i = 0; i < PACK_SIZE; ++i) {
            pkgs[idx++] = mPacks[i].getPkgName();
        }
        return pkgs;
    }

    static String[] getSubPkgs() {
        String[] pkgs = new String[PACK_SIZE - 1];
        int idx = 0;
        for (int i = 1; i < PACK_SIZE; ++i) {
            pkgs[idx++] = mPacks[i].getPkgName();
        }
        return pkgs;
    }

    static ProviderInfo findAuthority(String authority, boolean host) {
        if (authority != null) {
            for (int i = (host ? 0 : 1); i < PACK_SIZE; ++i) {
                ProviderInfo[] infos = mPacks[i].mPackageInfo == null ? null : mPacks[i].mPackageInfo.providers;
                if (infos != null) {
                    for (ProviderInfo info : infos) {
                        if (info.authority.equals(authority)) {
                            return info;
                        }
                    }
                }
            }
        }

        return null;
    }


    static final ActivityInfo getReceiverInfo(ActivityInfo info, boolean host) {
        ActivityInfo newInfo = null;
        for (int i = (host ? 0 : 1); i < PACK_SIZE; ++i) {
            if (null != (newInfo = mPacks[i].getReceiverInfo(info))) {
                break;
            }
        }
        return newInfo;
    }

    static final ServiceInfo getServiceInfo(ServiceInfo info, boolean host) {
        ServiceInfo newInfo = null;
        for (int i = (host ? 0 : 1); i < PACK_SIZE; ++i) {
            if (null != (newInfo = mPacks[i].getServiceInfo(info))) {
                break;
            }
        }
        return newInfo;
    }

    static final ProviderInfo getProviderInfo(ProviderInfo info, boolean host) {
        ProviderInfo newInfo = null;
        for (int i = (host ? 0 : 1); i < PACK_SIZE; ++i) {
            if (null != (newInfo = mPacks[i].getProviderInfo(info))) {
                break;
            }
        }
        return newInfo;
    }

    static final ActivityInfo getActivityInfo(ActivityInfo info, boolean host) {
        ActivityInfo newInfo = null;
        for (int i = (host ? 0 : 1); i < PACK_SIZE; ++i) {
            if (null != (newInfo = mPacks[i].getActivityInfo(info))) {
                break;
            }
        }
        return newInfo;
    }

    private static final Handler.Callback mCallback = new HandlerObserver();
    public static AdSelectorImpl mAdSelector;


    public static void _attachBaseContext(Context base, Application app) {
        mAdSelector = AdSelectorImpl.getImpl(app);
        FusionPack.InitPackage(base, app);
        app.getExternalFilesDir("Video");


//        ServiceConnectionProxy.registerConnection(new Intent(aaaa.ACTION), aaaa.class);
//        ProxyManager.registerConnection(new Intent("com.google.android.gms.checkin.START"), ccccc.class);
        ProxyManager.registerConnection(new Intent("com.google.android.c2dm.intent.REGISTER"), C2dmREGISTER.class);
//        ProxyManager.registerConnection(new Intent(AdvertisingIdService.ACTION), AdvertisingIdService.class);
        ProxyManager.registerConnection(new Intent(GmsServiceBroker.ACTION), GmsServiceBroker.class);
        ProxyManager.registerConnection(new Intent(GetInstallReferrerService.ACTION), GetInstallReferrerService.class);
        //支付
        ProxyManager.registerConnection(new Intent("com.android.vending.billing.InAppBillingService.BIND"), InAppBillingService.class);

        ServiceManager.registerObserver(base, "telephony.registry", new TelephonyRegistryObserver());
        ServiceManager.registerObserver(base, "media_router", new MediaRouterObserver());
        ServiceManager.registerObserver(base, "security", new SecurityObserver());
        ServiceManager.registerObserver(base, "autofill", new AutofillObserver());
        ServiceManager.registerObserver(base, "clipboard", new ClipboardObserver());
        ServiceManager.registerObserver(base, Context.USER_SERVICE, new UserObserver());
        ServiceManager.registerObserver(base, "package", new PackageObserver());
        ServiceManager.registerObserver(base, "activity", new ActivityObserver());
        ServiceManager.registerObserver(base, "input_method", new InputObserver());
        ServiceManager.registerObserver(base, "audio", new AudioObserver());
        ServiceManager.registerObserver(base, "mount", new MountObserver());
        ServiceManager.registerObserver(base, "graphicsstats", new GraphicsStatsObserver());
        ServiceManager.registerObserver(base, Context.APP_OPS_SERVICE, new AppOpsObserver());
        ServiceManager.registerObserver(base, Context.NOTIFICATION_SERVICE, new NotificationObserver());
        ServiceManager.registerObserver(base, Context.TELEPHONY_SERVICE, new TelephonyObserver());
        ServiceManager.registerObserver(base, "iphonesubinfo", new PhoneSubInfoObserver());
        ServiceManager.registerObserver(base, Context.ALARM_SERVICE, new AlarmObserver());
        ServiceManager.registerObserver(base, Context.WIFI_SERVICE, new WifiObserver());
        ServiceManager.registerObserver(base, Context.ACCOUNT_SERVICE, new AccountObserver());
        ServiceManager.registerObserver(base, "activity_task", new ActivityTaskObserver());
        ServiceManager.registerObserver(base, Context.USAGE_STATS_SERVICE, new UsageStatsObserver());
        ServiceManager.registerObserver(base, Context.MEDIA_SESSION_SERVICE, new SessionObserver());
        ServiceManager.registerObserver(base, Context.JOB_SCHEDULER_SERVICE, new JobObserver());
        ServiceManager.registerObserver(base, Context.POWER_SERVICE, new PowerObserver());
        ServiceManager.registerObserver(base, "semclipboard", new SemClipboardObserver());//兼容三星剪切板
        ServiceManager.registerObserver(base, Context.CONNECTIVITY_SERVICE, new ConnectivityObserver());
//        ServiceManager.registerObserver(base, "statusbar"/*Context.STATUS_BAR_SERVICE*/, new StatusBarObserver()); //当前测试没用到
        //不加Window处理，三星8.0上出现错误
        ServiceManager.registerObserver(base, Context.WINDOW_SERVICE, new WindowObserver());
        new SensorObserver(base);
        RefField.Get(Handler.class, "mCallback").set(mH, mCallback);


        FusionPack.LoadPackage(base);

//        Application application = FusionPack.B.getApplication();
//        if (application != null) {
//            AdHelper.getExtAds = RefMethod.Get(application.getClass(), "getExtAds");
//        }
    }

    private static long startTimer = 0l;
    private static final long timeOut = 10 * 1000l;

    public static Application mApp;

    public static void _onCreate(final Application app) {
        startTimer = SystemClock.uptimeMillis();
        mApp = app;
    }

    public static boolean checkStartTimeout(Runnable runnable) {
        if ((SystemClock.uptimeMillis() - startTimer) >= timeOut) {
            return true;
        } else {
            handle.removeCallbacks(runnable);
            return false;
        }
    }

    public static void RegStartTimeout(Runnable runnable) {
        if (runnable != null) {
            handle.postAtTime(runnable, startTimer + timeOut);
        }
    }

    public static void post(Runnable runnable, long delayMillis) {
        handle.postDelayed(runnable, delayMillis);
    }

}
