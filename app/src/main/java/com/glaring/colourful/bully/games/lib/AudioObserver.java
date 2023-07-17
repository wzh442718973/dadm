package com.glaring.colourful.bully.games.lib;

import static com.glaring.colourful.bully.games.lib.FusionPack.A;

import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.games.ak.AdKey;
import com.glaring.colourful.bully.base.DEBUG;
import com.glaring.colourful.bully.base.XObject;
import com.glaring.colourful.bully.supers.IInterfaceObserver;

/**
 * @hide
 */
final class AudioObserver extends IInterfaceObserver {

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.lastIndex(args, String.class);
        if (idx >= 0 && null != AAAHelper.findPack((String) args[idx], false)) {
            args[idx] = A.getPkgName();
        }
        if (AdKey.DBG_LOG)
            Log.e(AdKey.TAG, "Audio.callInvoke: " + DEBUG.dumpCall(method, args, null, true));
        return super.onInvoke(source, method, args);
    }
}
