package com.glaring.colourful.bully.games.lib;

import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.games.ak.AdKey;
import com.glaring.colourful.bully.base.DEBUG;
import com.glaring.colourful.bully.base.XObject;
import com.glaring.colourful.bully.supers.IInterfaceObserver;

class SessionObserver extends IInterfaceObserver {
    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, String.class);
        if (idx >= 0) {
            FusionPack pack = AAAHelper.findPack((String) args[idx], false);
            if (pack != null) {
                args[idx] = FusionPack.A.getPkgName();
            }
        }
        if (AdKey.DBG_LOG)
            Log.e(AdKey.TAG, "SessionManager.callInvoke: " + DEBUG.dumpCall(method, args, null, true));
        return super.onInvoke(source, method, args);
    }
}
