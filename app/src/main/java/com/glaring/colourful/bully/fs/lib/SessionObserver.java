package com.glaring.colourful.bully.fs.lib;

import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.ad.DEBUG;
import com.glaring.colourful.bully.ad.XObject;
import com.glaring.colourful.bully.mo.IInterfaceObserver;

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
