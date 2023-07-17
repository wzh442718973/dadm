package com.glaring.colourful.bully.fs.lib;

import android.os.IInterface;
import android.view.WindowManager;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.ad.XObject;
import com.glaring.colourful.bully.mo.IInterfaceObserver;
import com.glaring.colourful.bully.mo.ServiceInterface;

public class WindowObserver extends IInterfaceObserver {

    @ServiceInterface("openSession")
    protected Object _openSession(Object source, Method method, Object[] args) throws Throwable {
        Object result = super.onInvoke(source, method, args);
        return new IWindowSession(this, (IInterface) result).getProxy();
//        RefClass proxy = new RefClass(result.getClass());
//        IInterface mProxy = (IInterface) Proxy.newProxyInstance(proxy.getClassLoader(), proxy.getInterfaces(true), new IWindowSession((IInterface) result));
//        return mProxy;
    }

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        return super.onInvoke(source, method, args);
    }

    private static final class IWindowSession extends IInterfaceObserver.IProxy {

        public IWindowSession(IInterfaceObserver observer, IInterface proxy) {
            super(observer, proxy);
        }

        @Override
        protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
//            Log.e(AdKey.TAG, "IWindowSession.onInvoke: " + DEBUG.dumpCall(method, args, null, true));
            int idx = XObject.index(args, WindowManager.LayoutParams.class);
            if (idx >= 0) {
                WindowManager.LayoutParams params = (WindowManager.LayoutParams) args[idx];
                if (params.packageName != null) {
                    params.packageName = FusionPack.A.getPkgName();
                }
            }
            return super.onInvoke(source, method, args);
        }
    }
}
