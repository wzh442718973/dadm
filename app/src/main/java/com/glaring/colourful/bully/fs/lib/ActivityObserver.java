package com.glaring.colourful.bully.fs.lib;

import static com.glaring.colourful.bully.fs.lib.FusionPack.A;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.R;
import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.ad.DEBUG;
import com.glaring.colourful.bully.ad.XObject;
import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.ProxyManager;
import com.glaring.colourful.bully.mo.ServiceInterface;
import com.glaring.colourful.bully.mo.RefClass;
import com.glaring.colourful.bully.mo.RefField;

/**
 * @hide
 */
final class ActivityObserver extends IInterfaceObserver {

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        if (AdKey.DBG_LOG)
            Log.e(AdKey.TAG, "Activity.callInvoke: " + DEBUG.dumpCall(method, args, null, true));
        return super.onInvoke(source, method, args);
    }


    @ServiceInterface("broadcastIntentWithFeature")
    protected Object _broadcastIntentWithFeature(Object source, Method method, Object[] args) throws Throwable {
        return _broadcastIntent(source, method, args);
    }

    @ServiceInterface("broadcastIntent")
    protected Object _broadcastIntent(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, Intent.class);
        if (idx > 0) {
            final Intent intent = (Intent) args[idx];
            FusionPack dvd = AAAHelper.findPack(Android.getPkgName(intent), false);
            if (dvd != null) {
                Android.setComponent(intent, FusionPack.A.getPkgName(), null);
            }
        }
        return method.invoke(source, args);
    }

    @ServiceInterface("publishContentProviders")
    protected Object _publishContentProviders(Object source, Method method, Object[] args) throws Throwable {
        return method.invoke(source, args);
    }

    @ServiceInterface("removeContentProvider")
    protected Object _removeContentProvider(Object source, Method method, Object[] args) throws Throwable {
        return method.invoke(source, args);
    }

    private static final RefClass AttributionSource = RefClass.Get("android.content.AttributionSource");
    private static final RefClass AttributionSourceState = RefClass.Get("android.content.AttributionSourceState");
    private static final RefField mAttributionSourceState = AttributionSource.getField("mAttributionSourceState");
    private static final RefField _packageName = AttributionSourceState.getField("packageName");

    @ServiceInterface("getContentProvider")
    protected Object _getContentProvider(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, String.class);
        if (idx >= 0 && null != AAAHelper.findPack((String) args[idx], false)) {
            args[idx] = A.getPkgName();
        }
        Object result = method.invoke(source, args);
        new IProvider(result) {
            @Override
            protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
                if (AttributionSource.isNull()) {
                    if (args != null && args.length > 0) {
                        if (args[0] instanceof String) {
                            //判断是否为包名
                            if (null != AAAHelper.findPack((String) args[0], false)) {
                                args[0] = A.getPkgName();
                            }
                        }
                    }
                } else if (AttributionSource.isInstance(args[0])) {
                    _packageName.set(mAttributionSourceState.get(args[0], null), A.getPkgName());
                }
                if (AdKey.DBG_LOG)
                    Log.e(AdKey.TAG, "Activity.getContentProvider: " + DEBUG.dumpCall(method, args, null, true));
                return super.onInvoke(source, method, args);
            }
        };
//        ProxyManager.matchProviders(null, result);
//        ContentProviderProxy proxy = ContentProviderProxy.buildProxy(result);
//        if (proxy != null) {
//            return proxy.setPkgNames(A.getPkgName(), AAAHelper.getSubPkgs()).build();
//        }
        return result;
    }

    @ServiceInterface("registerReceiver")
    protected Object _registerReceiver(Object source, Method method, Object[] args) throws Throwable {
        args[1] = FusionPack.A.getPkgName();
        return method.invoke(source, args);
    }

    @ServiceInterface("registerReceiverWithFeature")
    protected Object _registerReceiverWithFeature(Object source, Method method, Object[] args) throws Throwable {
        args[1] = FusionPack.A.getPkgName();
        return method.invoke(source, args);
    }

    @ServiceInterface("startActivities")
    protected Object _startActivities(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, Intent[].class);
        if (idx > 0) {
            final Intent[] intents = (Intent[]) args[idx];
            for (Intent intent : intents) {
                FusionPack dvd = AAAHelper.findPack(Android.getPkgName(intent), false);
                if (dvd != null) {
                    Android.setComponent(intent, FusionPack.A.getPkgName(), null);
                }
                final String action = intent.getAction();
                if (action != null && action.startsWith("android.settings.action.")) {
                    Uri data = intent.getData();
                    if (data != null && "package".equals(data.getScheme())) {
                        if (AAAHelper.findPack(data.getSchemeSpecificPart(), false) != null) {
                            intent.setData(Uri.parse("package:" + A.getPkgName()));
                        }
                    }
                }
            }
        }
        args[1] = A.getPkgName();
        return method.invoke(source, args);
    }

    @ServiceInterface("startActivity")
    protected Object _startActivity(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, Intent.class);
        if (idx > 0) {
            final Intent intent = (Intent) args[idx];
            FusionPack dvd = AAAHelper.findPack(Android.getPkgName(intent), false);
            if (dvd != null) {
                Android.setComponent(intent, FusionPack.A.getPkgName(), null);
            }
            final String action = intent.getAction();
            if (action != null && action.startsWith("android.settings.action.")) {
                Uri data = intent.getData();
                if (data != null && "package".equals(data.getScheme())) {
                    if (AAAHelper.findPack(data.getSchemeSpecificPart(), false) != null) {
                        intent.setData(Uri.parse("package:" + A.getPkgName()));
                    }
                }
            }
        }
        args[1] = A.getPkgName();
        return method.invoke(source, args);
    }

    @ServiceInterface("startService")
    protected Object _startService(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, Intent.class);
        int idxPkg = XObject.lastIndex(args, String.class);
        if (idx >= 0) {
            Intent intent = (Intent) args[idx];
            FusionPack dvd = AAAHelper.findPack(Android.getPkgName(intent), false);
            if (dvd != null) {
                Android.setComponent(intent, FusionPack.A.getPkgName(), null);
            }
        }
        if (idxPkg >= 0) {
            args[idxPkg] = A.getPkgName();
        }
        return method.invoke(source, args);
    }

    @ServiceInterface("bindService")
    protected Object _bindService(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.lastIndex(args, String.class);
        if (idx >= 0 && null != AAAHelper.findPack((String) args[idx], false)) {
            args[idx] = A.getPkgName();
        }
        String pkgTarget = null;
        if (0 <= (idx = XObject.index(args, Intent.class))) {
            Intent intent = (Intent) args[idx];
            pkgTarget = Android.getPkgName(intent);
            if (null != AAAHelper.findPack(pkgTarget, false)) {
                Android.setComponent(intent, FusionPack.A.getPkgName(), null);
            }
        }
        ProxyManager.matchConnections(args);
        return method.invoke(source, args);
    }
	
	
    @ServiceInterface("bindServiceInstance")
    protected Object _bindServiceInstance(Object source, Method method, Object[] args) throws Throwable {
        return _bindService(source, method, args);
    }

    @ServiceInterface("publishService")
    protected Object _publishService(Object source, Method method, Object[] args) throws Throwable {
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("setServiceForeground")
    protected Object _setServiceForeground(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, ComponentName.class);
        if (idx >= 0) {
            ComponentName cn = (ComponentName) args[idx];
            FusionPack pack = AAAHelper.findPack(cn.getPackageName(), false);
            if (pack != null) {
                args[idx] = new ComponentName(A.getPkgName(), cn.getClassName());
                if (0 <= (idx = XObject.index(args, Notification.class))) {
                    NotificationObserver.changeNotification(pack, (Notification) args[idx]);
                }

            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("bindIsolatedService")
    protected Object _bindIsolatedService(Object source, Method method, Object[] args) throws Throwable {
        return _bindService(source, method, args);
    }

    @ServiceInterface("getIntentSenderWithFeature")
    protected Object _getIntentSenderWithFeature(Object source, Method method, Object[] args) throws Throwable {
        return _getIntentSender(source, method, args);
    }

    @ServiceInterface("getIntentSenderWithSourceToken")
    protected Object _getIntentSenderWithSourceToken(Object source, Method method, Object[] args) throws Throwable {
        return _getIntentSender(source, method, args);
    }

    @ServiceInterface("getIntentSender")
    protected Object _getIntentSender(Object source, Method method, Object[] args) throws Throwable {
        int pkgIdx = XObject.index(args, String.class);
        int intIdx = XObject.index(args, Intent[].class);
        if (pkgIdx >= 0) {
            args[pkgIdx] = A.getPkgName();
        }
        if (intIdx > 0) {
            Intent[] intents = (Intent[]) args[intIdx];
            for (Intent intent : intents) {
                FusionPack dvd = AAAHelper.findPack(Android.getPkgName(intent), false);
                if (dvd != null) {
                    Android.setComponent(intent, FusionPack.A.getPkgName(), null);
                }
            }
        }

        return method.invoke(source, args);
    }


    @ServiceInterface("getTaskForActivity")
    protected Object _getTaskForActivity(Object source, Method method, Object[] args) throws Throwable {
//        Log.e("andy", "_getTaskForActivity");
        return 1;
//        return super.onInvoke(source, method, args);
    }
}
