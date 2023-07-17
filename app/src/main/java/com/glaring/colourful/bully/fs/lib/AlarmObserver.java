package com.glaring.colourful.bully.fs.lib;

import static com.glaring.colourful.bully.fs.lib.FusionPack.A;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.ServiceInterface;

public class AlarmObserver extends IInterfaceObserver {

    @ServiceInterface("set")
    protected Object _set(Object source, Method method, Object[] args) throws Throwable {
        if (args[0] instanceof String) {
            if (null != AAAHelper.findPack((String) args[0], false)) {
                args[0] = A.getPkgName();
            }
        }
        return super.onInvoke(source, method, args);
    }
}
