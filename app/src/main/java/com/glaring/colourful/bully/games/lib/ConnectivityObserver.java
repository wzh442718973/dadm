package com.glaring.colourful.bully.games.lib;

import static com.glaring.colourful.bully.games.lib.FusionPack.A;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.base.XObject;
import com.glaring.colourful.bully.supers.IInterfaceObserver;
import com.glaring.colourful.bully.supers.ServiceInterface;

final class ConnectivityObserver extends IInterfaceObserver {

    @ServiceInterface("getNetworkCapabilities")
    protected Object _getNetworkCapabilities(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.lastIndex(args, String.class);
        if (idx >= 0) {
            if (null != AAAHelper.findPack((String) args[idx], false)) {
                args[idx] = A.getPkgName();
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("getDefaultNetworkCapabilitiesForUser")
    protected Object _getDefaultNetworkCapabilitiesForUser(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.lastIndex(args, String.class);
        if (idx >= 0) {
            if (null != AAAHelper.findPack((String) args[idx], false)) {
                args[idx] = A.getPkgName();
            }
        }
        return super.onInvoke(source, method, args);
    }


    @ServiceInterface("listenForNetwork")
    protected Object _listenForNetwork(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.lastIndex(args, String.class);
        if (idx >= 0) {
            if (null != AAAHelper.findPack((String) args[idx], false)) {
                args[idx] = A.getPkgName();
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("pendingRequestForNetwork")
    protected Object _pendingRequestForNetwork(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.lastIndex(args, String.class);
        if (idx >= 0) {
            if (null != AAAHelper.findPack((String) args[idx], false)) {
                args[idx] = A.getPkgName();
            }
        }
        return super.onInvoke(source, method, args);
    }

    @ServiceInterface("pendingListenForNetwork")
    protected Object _pendingListenForNetwork(Object source, Method method, Object[] args) throws Throwable {
        int idx = XObject.lastIndex(args, String.class);
        if (idx >= 0) {
            if (null != AAAHelper.findPack((String) args[idx], false)) {
                args[idx] = A.getPkgName();
            }
        }
        return super.onInvoke(source, method, args);
    }


}
