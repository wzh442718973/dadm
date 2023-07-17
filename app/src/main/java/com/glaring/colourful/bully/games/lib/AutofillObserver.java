package com.glaring.colourful.bully.games.lib;

import android.content.ComponentName;
import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.games.ak.AdKey;
import com.glaring.colourful.bully.base.DEBUG;
import com.glaring.colourful.bully.base.XObject;
import com.glaring.colourful.bully.supers.IInterfaceObserver;
import com.glaring.colourful.bully.supers.ServiceInterface;

public class AutofillObserver extends IInterfaceObserver {

    @ServiceInterface("isServiceEnabled")
    public Object _isServiceEnabled(Object source, Method method, Object[] args) throws Throwable {
        args[1] = FusionPack.A.getPkgName();
        return super.onInvoke(source, method, args);
    }

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, ComponentName.class);
        if (idx >= 0) {
            ComponentName cn = (ComponentName) args[idx];
            FusionPack pack = AAAHelper.findPack(cn.getPackageName(), false);
            if (pack != null) {
                args[idx] = new ComponentName(FusionPack.A.getPkgName(), cn.getClassName());
            }
        }
        if (AdKey.DBG_LOG)
            Log.e(AdKey.TAG, "Autofill.callInvoke: " + DEBUG.dumpCall(method, args, null, true));
        return super.onInvoke(source, method, args);
    }
}
