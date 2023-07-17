package com.glaring.colourful.bully.fs.lib;

import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.mo.RefClass;
import com.glaring.colourful.bully.mo.RefField;
import com.glaring.colourful.bully.mo.RefMethod;

/**
 * @hide
 */
final class Android {

    //==========================ActivityThread$H===============================
    static final int EXECUTE_TRANSACTION;
    static final int LAUNCH_ACTIVITY;
    static final int CREATE_SERVICE;
    static final int BIND_SERVICE;
    static final int RECEIVER;
    static final int INSTALL_PROVIDER;

    //=========================ActivityThread==========================================
    static final RefClass ActivityThread = RefClass.Get("android.app.ActivityThread");
    static final RefMethod installContentProviders = ActivityThread.getMethod("installContentProviders", Context.class, List.class);
    static final RefField mInitialApplication = ActivityThread.getField("mInitialApplication");
    static final RefMethod getInstrumentation = ActivityThread.getMethod("getInstrumentation");
    static final Object mActivityThread = ActivityThread.getMethod("currentActivityThread").call(null);
    static final Object mBoundApplication = ActivityThread.getField("mBoundApplication").get(mActivityThread, null);
    static final Object mPackages = ActivityThread.getField("mPackages").get(mActivityThread, null);
    static final Handler mH = ActivityThread.getField("mH").get(mActivityThread, null);


    static final RefClass AppBindData = ActivityThread.getSubClass("AppBindData");
    static final RefField AppBindData_info = AppBindData.getField("info");
    static final RefField AppBindData_appInfo = AppBindData.getField("appInfo");
    static final RefField AppBindData_providers = AppBindData.getField("providers");


    static final RefClass ActivityClientRecord = ActivityThread.getSubClass("ActivityClientRecord");
    static final RefField ActivityClientRecord_activityInfo = ActivityClientRecord.getField("activityInfo");
    static final RefField ActivityClientRecord_intent = ActivityClientRecord.getField("intent");

    static final RefClass ReceiverData = ActivityThread.getSubClass("ReceiverData");
    static final RefField ReceiverData_info = ReceiverData.getField("info");
    static final RefField ReceiverData_intent = ReceiverData.getField("intent");

    static final RefClass CreateServiceData = ActivityThread.getSubClass("CreateServiceData");
    static final RefField CreateServiceData_info = CreateServiceData.getField("info");
    static final RefField CreateServiceData_intent = CreateServiceData.getField("intent");

    static final RefClass BindServiceData = ActivityThread.getSubClass("BindServiceData");
    static final RefClass ProviderKey = ActivityThread.getSubClass("ProviderKey");

    static final RefMethod callApplicationOnCreate = RefMethod.Get(Instrumentation.class, "callApplicationOnCreate", Application.class);


    static final RefClass ContextImpl = RefClass.Get("android.app.ContextImpl");
    static final RefField ContextImpl_mPackageInfo = ContextImpl.getField("mPackageInfo");
    static final RefField ContextImpl_mBasePackageName = ContextImpl.getField("mBasePackageName");
    static final RefField ContextImpl_mOpPackageName = ContextImpl.getField("mOpPackageName");

    static final RefClass LoadedApk = RefClass.Get("android.app.LoadedApk");
    static final RefMethod makeApplication = LoadedApk.getMethod("makeApplication", boolean.class, Instrumentation.class);


    static final RefClass ApplicationContentResolver = ContextImpl.getSubClass("ApplicationContentResolver");
    static final RefField ContentResolver_mPackageName = RefField.Get(ContentResolver.class, "mPackageName");


    static final RefClass ArrayMap = RefClass.Get("android.util.ArrayMap");


    //=================================ApplicationInfo====================================
//    static final RefField primaryCpuAbi = RefField.Get(ApplicationInfo.class, "primaryCpuAbi");
//    static final RefField seInfoUser = RefField.Get(ApplicationInfo.class, "seInfoUser");
//    static final RefField credentialEncryptedDataDir = RefField.Get(ApplicationInfo.class, "credentialEncryptedDataDir");
//    static final RefField credentialProtectedDataDir = RefField.Get(ApplicationInfo.class, "credentialProtectedDataDir");
//    static final RefField nativeLibraryRootDir = RefField.Get(ApplicationInfo.class, "nativeLibraryRootDir");
//    static final RefField deviceEncryptedDataDir = RefField.Get(ApplicationInfo.class, "deviceEncryptedDataDir");
//    static final RefField deviceProtectedDataDir = RefField.Get(ApplicationInfo.class, "deviceProtectedDataDir");
//    static final RefField splitPublicSourceDirs = RefField.Get(ApplicationInfo.class, "splitPublicSourceDirs");
//    static final RefField splitClassLoaderNames = RefField.Get(ApplicationInfo.class, "splitClassLoaderNames");
//    static final RefField splitNames = RefField.Get(ApplicationInfo.class, "splitNames");
//    static final RefField splitSourceDirs = RefField.Get(ApplicationInfo.class, "splitSourceDirs");
//    static final RefField scanPublicSourceDir = RefField.Get(ApplicationInfo.class, "scanPublicSourceDir");
//    static final RefField scanSourceDir = RefField.Get(ApplicationInfo.class, "scanSourceDir");
//    static final RefField sharedLibraryFiles = RefField.Get(ApplicationInfo.class, "sharedLibraryFiles");
//
//    static final RefField installLocation = RefField.Get(PackageInfo.class, "installLocation");
//    static final RefField splitNames2 = RefField.Get(PackageInfo.class, "splitNames");
//    static final RefField splitRevisionCodes = RefField.Get(PackageInfo.class, "splitRevisionCodes");

    static {
        RefClass H = ActivityThread.getSubClass("H");
        RefField _LAUNCH_ACTIVITY = H.getField("LAUNCH_ACTIVITY");
        RefField _CREATE_SERVICE = H.getField("CREATE_SERVICE");
        RefField _RECEIVER = H.getField("RECEIVER");
        RefField _BIND_SERVICE = H.getField("BIND_SERVICE");
        RefField _INSTALL_PROVIDER = H.getField("INSTALL_PROVIDER");
        RefField _EXECUTE_TRANSACTION = H.getField("EXECUTE_TRANSACTION");
        if (_CREATE_SERVICE.isNull()) {
            Log.e(AdKey.TAG, "-----CREATE SERVICE-----");
            System.exit(0);
        }
        if (_RECEIVER.isNull()) {
            Log.e(AdKey.TAG, "-----RECEIVER-----");
            System.exit(0);
        }
        if (_BIND_SERVICE.isNull()) {
            Log.e(AdKey.TAG, "-----BIND SERVICE-----");
            System.exit(0);
        }
        if (_INSTALL_PROVIDER.isNull()) {
            Log.e(AdKey.TAG, "-----INSTALL PROVIDER-----");
            System.exit(0);
        }
        if (Build.VERSION.SDK_INT >= 29) {
            EXECUTE_TRANSACTION = _EXECUTE_TRANSACTION.get(null, 159);//-1);
        } else {
            EXECUTE_TRANSACTION = _EXECUTE_TRANSACTION.get(null, -1);
        }
        LAUNCH_ACTIVITY = _LAUNCH_ACTIVITY.get(null, -1);
        CREATE_SERVICE = _CREATE_SERVICE.get(null, -1);
        RECEIVER = _RECEIVER.get(null, -1);
        BIND_SERVICE = _BIND_SERVICE.get(null, -1);
        INSTALL_PROVIDER = _INSTALL_PROVIDER.get(null, -1);

        if (AdKey.DBG_LOG) {
            Log.e(AdKey.TAG, "EXECUTE_TRANSACTION=" + EXECUTE_TRANSACTION);
            Log.e(AdKey.TAG, "LAUNCH_ACTIVITY=" + LAUNCH_ACTIVITY);
            Log.e(AdKey.TAG, "CREATE_SERVICE=" + CREATE_SERVICE);
            Log.e(AdKey.TAG, "RECEIVER=" + RECEIVER);
            Log.e(AdKey.TAG, "BIND_SERVICE=" + BIND_SERVICE);
            Log.e(AdKey.TAG, "INSTALL_PROVIDER=" + INSTALL_PROVIDER);
        }
    }


    static void clearSystemProvider() {
        RefClass ArrayMap = RefClass.Get("android.util.ArrayMap");
        RefMethod clear = ArrayMap.getMethod("clear");
        Object mProviderMap = ActivityThread.getField("mProviderMap").get(mActivityThread, null);
        Object mProviderRefCountMap = ActivityThread.getField("mProviderRefCountMap").get(mActivityThread, null);
        Object mGetProviderLocks = ActivityThread.getField("mGetProviderLocks").get(mActivityThread, null);

        if (ArrayMap.isInstance(mProviderMap)) {
            clear.call(mProviderMap);
            clear.call(mProviderRefCountMap);
            clear.call(mGetProviderLocks);
        }

        try {


            RefClass HostClass = new RefClass(Settings.class).getSubClass("ContentProviderHolder");
            String fieldName = "mContentProvider";  //IContentProvider mContentProvider
            String hostVarName = "sProviderHolder"; //ContentProviderHolder sProviderHolder

            if (HostClass.isNull()) {
                HostClass = new RefClass(Settings.class).getSubClass("NameValueCache");
                fieldName = "mContentProvider"; //IContentProvider mContentProvider
                hostVarName = "sNameValueCache";    //NameValueCache sNameValueCache
            }
            final RefField mContentProvider = HostClass.getField(fieldName);
            if (mContentProvider.isNull()) {
                Log.e(AdKey.TAG, "Settings兼容测试");
                return;
            }
            //清除之前的缓冲
            Class[] subClasss = Settings.class.getDeclaredClasses();
            if (subClasss != null) {
                for (Class subClass : subClasss) {
                    Object sProviderHolder = new RefClass(subClass).getField(hostVarName).get(null, null);
                    if (sProviderHolder != null) {
                        mContentProvider.set(sProviderHolder, null);
                    }
                }
            }
        } catch (Throwable e) {

        }
    }

    static Object getLoadedApk(Context context) {
        while (context != null) {
            if (ContextImpl.isInstance(context)) {
                return ContextImpl_mPackageInfo.get(context, null);
            } else if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                context = null;
            }
        }
        return null;
    }


    static void repairFull(PackageInfo info) {
        if (info.activities != null) {
            for (ActivityInfo _info : info.activities) {
                _info.applicationInfo = info.applicationInfo;
            }
        }
        if (info.providers != null) {
            for (ProviderInfo _info : info.providers) {
                _info.applicationInfo = info.applicationInfo;
            }
        }
        if (info.receivers != null) {
            for (ActivityInfo _info : info.receivers) {
                _info.applicationInfo = info.applicationInfo;
            }
        }
        if (info.services != null) {
            for (ServiceInfo _info : info.services) {
                _info.applicationInfo = info.applicationInfo;
            }
        }
    }

    public static String getPkgName(Intent intent) {
        final ComponentName cn = intent.getComponent();
        return cn != null ? cn.getPackageName() : intent.getPackage();
    }

    /**
     * pkgName, clsName == null, 保持原有
     *
     * @param intent
     * @param pkgName
     * @param clsName
     * @return
     */
    public static Intent setComponent(Intent intent, String pkgName, String clsName) {
        final ComponentName cn = intent.getComponent();
        if (cn != null) {
            intent.setComponent(new ComponentName(pkgName == null ? cn.getPackageName() : pkgName, clsName == null ? cn.getClassName() : clsName));
        }
        if (intent.getPackage() != null) {
            intent.setPackage(pkgName);
        }
        return intent;
    }
}
