package com.glaring.colourful.bully.fs.lib;

import static com.glaring.colourful.bully.fs.lib.FusionPack.A;

import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.ad.DEBUG;
import com.glaring.colourful.bully.ad.XObject;
import com.glaring.colourful.bully.mo.IInterfaceObserver;

public class AccountObserver extends IInterfaceObserver {
    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.lastIndex(args, String.class);
        if (idx >= 0) {
            if (null != AAAHelper.findPack((String) args[idx], false)) {
                args[idx] = A.getPkgName();
            }
        }
        if (AdKey.DBG_LOG)
            Log.e(AdKey.TAG, "Account.callInvoke: " + DEBUG.dumpCall(method, args, null, true));
        return super.onInvoke(source, method, args);
    }
}
