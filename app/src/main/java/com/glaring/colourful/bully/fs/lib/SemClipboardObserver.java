package com.glaring.colourful.bully.fs.lib;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.ad.XObject;
import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.ServiceInterface;

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
