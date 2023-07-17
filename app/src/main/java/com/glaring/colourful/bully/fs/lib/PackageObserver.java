package com.glaring.colourful.bully.fs.lib;

import static com.glaring.colourful.bully.fs.lib.FusionPack.A;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Process;
import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.ad.DEBUG;
import com.glaring.colourful.bully.ad.XObject;
import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.ServiceInterface;

/**
 * @hide
 */
final class PackageObserver extends IInterfaceObserver {
    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        if (AdKey.DBG_LOG)
            Log.e(AdKey.TAG, "Package.onInvoke: " + DEBUG.dumpCall(method, args, null, true));
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getInstallerPackageName")
    protected Object _getInstallerPackageName(Object source, Method method, Object[] args) throws Throwable {
        return "com.android.vending";
    }

    @ServiceInterface("isPermissionRevokedByPolicy")
    protected Object _isPermissionRevokedByPolicy(Object source, Method method, Object[] args) throws Throwable {
        FusionPack pack = AAAHelper.findPack((String) args[1], false);
        if (pack != null) {
            args[1] = A.getPkgName();
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getActivityInfo")
    protected Object _getActivityInfo(Object source, Method method, Object[] args) throws Throwable {
        final int idx = XObject.index(args, ComponentName.class);
        if (idx >= 0) {
            final ComponentName cn = (ComponentName) args[idx];
            FusionPack pack = AAAHelper.findPack(cn.getPackageName(), false);
            if (pack != null) {
                args[idx] = new ComponentName(A.getPkgName(), cn.getClassName());
                ActivityInfo info = (ActivityInfo) super.onInvoke(source, method, args);
                ActivityInfo newInfo = pack.getActivityInfo(info);
                if (newInfo != null) {
                    return newInfo;
                }
                Log.e(AdKey.TAG, "fusion error!" + cn);
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getReceiverInfo")
    protected Object _getReceiverInfo(Object source, Method method, Object[] args) throws Throwable {
        final int idx = XObject.index(args, ComponentName.class);
        if (idx >= 0) {
            final ComponentName cn = (ComponentName) args[idx];
            FusionPack pack = AAAHelper.findPack(cn.getPackageName(), false);
            if (pack != null) {
                args[idx] = new ComponentName(A.getPkgName(), cn.getClassName());
                ActivityInfo info = (ActivityInfo) super.onInvoke(source, method, args);
                ActivityInfo newInfo = pack.getReceiverInfo(info);
                if (newInfo != null) {
                    return newInfo;
                }
                Log.e(AdKey.TAG, "fusion error!" + cn);
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getServiceInfo")
    protected Object _getServiceInfo(Object source, Method method, Object[] args) throws Throwable {
        final int idx = XObject.index(args, ComponentName.class);
        if (idx >= 0) {
            final ComponentName cn = (ComponentName) args[idx];
            FusionPack pack = AAAHelper.findPack(cn.getPackageName(), false);
            if (pack != null) {
                args[idx] = new ComponentName(A.getPkgName(), cn.getClassName());
                ServiceInfo info = (ServiceInfo) super.onInvoke(source, method, args);
                ServiceInfo newInfo = pack.getServiceInfo(info);
                if (newInfo != null) {
                    return newInfo;
                }
                Log.e(AdKey.TAG, "fusion error!" + cn);
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getProviderInfo")
    protected Object _getProviderInfo(Object source, Method method, Object[] args) throws Throwable {
        final int idx = XObject.index(args, ComponentName.class);
        if (idx >= 0) {
            final ComponentName cn = (ComponentName) args[idx];
            FusionPack pack = AAAHelper.findPack(cn.getPackageName(), false);
            if (pack != null) {
                args[idx] = new ComponentName(A.getPkgName(), cn.getClassName());
                ProviderInfo info = (ProviderInfo) super.onInvoke(source, method, args);
                ProviderInfo newInfo = pack.getProviderInfo(info);
                if (newInfo != null) {
                    return newInfo;
                }
                Log.e(AdKey.TAG, "fusion error!" + cn);
            }
        }
        return super.onInvoke(source, method, args);
    }


    @ServiceInterface("resolveContentProvider")
    protected Object _resolveContentProvider(Object source, Method method, Object[] args) throws Throwable {
        String name = (String) args[0];
        ProviderInfo info = AAAHelper.findAuthority(name, false);
        if (info != null) {
            return info;
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("queryContentProviders")
    protected Object _queryContentProviders(Object source, Method method, Object[] args) throws Throwable {
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getPackageUid")
    protected Object _getPackageUid(Object source, Method method, Object[] args) throws Throwable {
        if (null != AAAHelper.findPack((String) args[0], true)) {
            return UID;
        }
        return super.onInvoke(source, method, args);
    }

    final int UID = Process.myUid();

    @ServiceInterface("getPackagesForUid")
    public Object _getPackagesForUid(Object source, Method method, Object[] args) throws Throwable {
        final int uid = (int) args[0];
        if (UID == uid) {
            return AAAHelper.getAllPkgs();
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("addPackageDependency")
    protected Object _addPackageDependency(Object source, Method method, Object[] args) throws Throwable {
        FusionPack pack = AAAHelper.findPack((String) args[0], false);
        if (pack != null) {
            return null;
        }
        return method.invoke(source, args);
    }

    @ServiceInterface("notifyDexLoad")
    public Object _notifyDexLoad(Object source, Method method, Object[] args) throws Throwable {
        FusionPack pack = AAAHelper.findPack((String) args[0], false);
        if (pack != null) {
            return null;
        }
        return method.invoke(source, args);
    }

    @ServiceInterface("notifyPackageUse")
    public Object _notifyPackageUse(Object source, Method method, Object[] args) throws Throwable {
        FusionPack pack = AAAHelper.findPack((String) args[0], false);
        if (pack != null) {
            return null;
        }
        return method.invoke(source, args);
    }

    @ServiceInterface("getApplicationInfo")
    public Object _getApplicationInfo(Object source, Method method, Object[] args) throws Throwable {
        FusionPack dvd = AAAHelper.findPack((String) args[0], false);
        if (dvd != null) {
            return dvd.mInfo;
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getPackageInfo")
    public Object _getPackageInfo(Object source, Method method, Object[] args) throws Throwable {
        FusionPack dvd = AAAHelper.findPack((String) args[0], false);
        if (dvd != null) {
            return dvd.mPackageInfo;//info.mContext.getPackageManager().getPackageArchiveInfo(info.mResFile.getPath(), flags);
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("queryIntentActivities")
    protected Object _queryIntentActivities(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, Intent.class);
        if (idx >= 0) {
            Intent intent = (Intent) args[idx];
            FusionPack dvd = AAAHelper.findPack(Android.getPkgName(intent), false);
            if (dvd != null) {
                Android.setComponent(intent, FusionPack.A.getPkgName(), null);
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("queryIntentServices")
    protected Object _queryIntentServices(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, Intent.class);
        if (idx >= 0) {
            Intent intent = (Intent) args[idx];
            FusionPack dvd = AAAHelper.findPack(Android.getPkgName(intent), false);
            if (dvd != null) {
                Android.setComponent(intent, FusionPack.A.getPkgName(), null);
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("resolveIntent")
    protected Object _resolveIntent(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, Intent.class);
        if (idx >= 0) {
            Intent intent = (Intent) args[idx];
            FusionPack dvd = AAAHelper.findPack(Android.getPkgName(intent), false);
            if (dvd != null) {
                Android.setComponent(intent, FusionPack.A.getPkgName(), null);
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getComponentEnabledSetting")
    protected Object _getComponentEnabledSetting(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, ComponentName.class);
        if (idx >= 0) {
            ComponentName cn = (ComponentName) args[idx];
            FusionPack pack = AAAHelper.findPack(cn.getPackageName(), false);
            if (pack != null) {
                args[idx] = new ComponentName(A.getPkgName(), cn.getClassName());
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("setComponentEnabledSetting")
    protected Object _setComponentEnabledSetting(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, ComponentName.class);
        if (idx >= 0) {
            ComponentName cn = (ComponentName) args[idx];
            FusionPack pack = AAAHelper.findPack(cn.getPackageName(), false);
            if (pack != null) {
                args[idx] = new ComponentName(A.getPkgName(), cn.getClassName());
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getTargetSdkVersionForPackage")
    public Object _getTargetSdkVersionForPackage(Object source, Method method, Object[] args) throws Throwable {
        FusionPack dvd = AAAHelper.findPack((String) args[0], false);
        if (dvd != null) {
            args[0] = A.getPkgName();
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("requestChecksums")
    public Object _requestChecksums(Object source, Method method, Object[] args) throws Throwable {
        FusionPack dvd = AAAHelper.findPack((String) args[0], false);
        if (dvd != null) {
            args[0] = A.getPkgName();
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("requestPackageChecksums")
    public Object _requestPackageChecksums(Object source, Method method, Object[] args) throws Throwable {
        FusionPack dvd = AAAHelper.findPack((String) args[0], false);
        if (dvd != null) {
            args[0] = A.getPkgName();
        }
        return super.onInvoke(source, method, args);
    }

}
