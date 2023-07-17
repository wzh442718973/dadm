package com.glaring.colourful.bully.games.lib;

import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.games.ak.AdKey;
import com.glaring.colourful.bully.base.DEBUG;
import com.glaring.colourful.bully.base.XObject;
import com.glaring.colourful.bully.supers.IInterfaceObserver;

/**
 * @hide
 */
final class AppOpsObserver extends IInterfaceObserver {
    private static final boolean DBG_LOG = AdKey.DBG_LOG;

    private static final int OP_WRITE_SETTINGS = 23;
    private static final int OP_SYSTEM_ALERT_WINDOW = 24;
    private static final int OP_ACCESS_NOTIFICATIONS = 25;

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        if (DBG_LOG)
            Log.e(AdKey.TAG, "AppOps.callInvoke: " + DEBUG.dumpCall(method, args, null, true));
        int idx = XObject.index(args, String.class);
        if (idx >= 0 && null != AAAHelper.findPack((String) args[idx], false)) {
            args[idx] = FusionPack.A.getPkgName();
        }
        return super.onInvoke(source, method, args);
    }
//
//    @ServiceInterface("noteOperation")
//    protected Object _noteOperation(Object source, Method method, Object[] args) throws Throwable {
//        return AppOpsManager.MODE_ALLOWED;
//    }
//
//    @ServiceInterface("checkOperation")
//    protected Object _checkOperation(Object source, Method method, Object[] args) throws Throwable {
//        final int op = (int) args[0];
//        if (OP_SYSTEM_ALERT_WINDOW == op || OP_WRITE_SETTINGS == op) {
//            return super.onInvoke(source, method, args);
//        }
//        return AppOpsManager.MODE_ALLOWED;
//    }
//
//    @ServiceInterface("checkPackage")
//    protected Object _checkPackage(Object source, Method method, Object[] args) throws Throwable {
//        return AppOpsManager.MODE_ALLOWED;
//    }
//
//    @ServiceInterface("startOperation")
//    protected Object _startOperation(Object source, Method method, Object[] args) throws Throwable {
//        return AppOpsManager.MODE_ALLOWED;
//    }
//
//    @ServiceInterface("noteProxyOperation")
//    protected Object _noteProxyOperation(Object source, Method method, Object[] args) throws Throwable {
//        return AppOpsManager.MODE_ALLOWED;
//    }
//
//    @ServiceInterface("finishOperation")
//    protected Object _finishOperation(Object source, Method method, Object[] args) throws Throwable {
//        return null;
//    }
//
//    @ServiceInterface("startWatchingMode")
//    protected Object _startWatchingMode(Object source, Method method, Object[] args) throws Throwable {
//        return null;
//    }
//
//    @ServiceInterface("stopWatchingMode")
//    protected Object _stopWatchingMode(Object source, Method method, Object[] args) throws Throwable {
//        return null;
//    }
//
//    @ServiceInterface("getOpsForPackage")
//    protected Object _getOpsForPackage(Object source, Method method, Object[] args) throws Throwable {
//        return new ArrayList<>(0);
//    }
//
//    @ServiceInterface("setMode")
//    protected Object _setMode(Object source, Method method, Object[] args) throws Throwable {
//        return null;
//    }
//
//    @ServiceInterface("resetAllModes")
//    protected Object _resetAllModes(Object source, Method method, Object[] args) throws Throwable {
//        return null;
//    }
//
//    @ServiceInterface("checkAudioOperation")
//    protected Object _checkAudioOperation(Object source, Method method, Object[] args) throws Throwable {
//        return AppOpsManager.MODE_ALLOWED;
//    }
}
