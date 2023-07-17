package com.glaring.colourful.bully.games.lib;

import static com.glaring.colourful.bully.games.lib.FusionPack.A;

import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.games.ak.AdKey;
import com.glaring.colourful.bully.base.DEBUG;
import com.glaring.colourful.bully.base.XObject;
import com.glaring.colourful.bully.supers.IInterfaceObserver;
import com.glaring.colourful.bully.supers.ServiceInterface;

public class TelephonyRegistryObserver extends IInterfaceObserver {
//    @Override
//    protected Object onInvoke(Object source, String methodName, Method method, Object[] args) throws Throwable {
//        return super.onInvoke(source, methodName, method, args);
//    }

    @ServiceInterface("listenForSubscriber")
    protected Object _listenForSubscriber(Object source, Method method, Object[] args) throws Throwable {
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
