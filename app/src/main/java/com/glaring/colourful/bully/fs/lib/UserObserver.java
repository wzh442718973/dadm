package com.glaring.colourful.bully.fs.lib;

import static com.glaring.colourful.bully.fs.lib.FusionPack.A;

import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.ad.DEBUG;
import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.ServiceInterface;

public class UserObserver extends IInterfaceObserver {

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        if (AdKey.DBG_LOG)
            Log.e(AdKey.TAG, "User.onInvoke: " + DEBUG.dumpCall(method, args, null, true));
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getApplicationRestrictions")
    protected Object _getApplicationRestrictions(Object source, Method method, Object[] args) throws Throwable {
        args[0] = A.getPkgName();
        return super.onInvoke(source, method, args);
    }


}
