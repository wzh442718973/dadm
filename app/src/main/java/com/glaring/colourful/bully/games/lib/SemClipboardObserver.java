package com.glaring.colourful.bully.games.lib;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.base.XObject;
import com.glaring.colourful.bully.supers.IInterfaceObserver;
import com.glaring.colourful.bully.supers.ServiceInterface;

public class SemClipboardObserver extends IInterfaceObserver {


    @ServiceInterface("getClipData")
    protected Object _getClipData(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.index(args, String.class);
        if (idx >= 0) {
            FusionPack pack = AAAHelper.findPack((String) args[idx], false);
            if (pack != null) {
                args[idx] = FusionPack.A.getPkgName();
            }
        }
        return super.onInvoke(source, method, args);
    }
}
