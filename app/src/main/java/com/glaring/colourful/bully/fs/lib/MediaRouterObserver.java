package com.glaring.colourful.bully.fs.lib;

import static com.glaring.colourful.bully.fs.lib.FusionPack.A;

import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.ad.DEBUG;
import com.glaring.colourful.bully.ad.XObject;
import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.ServiceInterface;

public class MediaRouterObserver extends IInterfaceObserver {

    @ServiceInterface("registerClientAsUser")
    protected Object _registerClientAsUser(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, String.class);
        if (idx >= 0) {
            FusionPack pack = AAAHelper.findPack((String) args[idx], false);
            if (pack != null) {
                args[idx] = A.getPkgName();
            }
        }
        if (AdKey.DBG_LOG)
            Log.e(AdKey.TAG, this.getName() + ".onInvoke: " + DEBUG.dumpCall(method, args, (Object) null, true));
        return super.onInvoke(source, method, args);
    }
}
