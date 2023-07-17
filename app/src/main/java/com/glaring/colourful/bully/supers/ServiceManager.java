package com.glaring.colourful.bully.supers;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.glaring.colourful.bully.base.DEBUG;

/**
 * Created by Administrator on 2017/7/11.
 */

public final class ServiceManager {

    private static final RefMethod getService;
    private static final RefMethod listServices;
    private static final Map<String, IBinder> sCache;

    static {
        RefClass _ServiceManager = RefClass.Get("android.os.ServiceManager");
        getService = _ServiceManager.getMethod("getService", String.class);
        listServices = _ServiceManager.getMethod("listServices");
        sCache = _ServiceManager.getField("sCache").get(null, new HashMap<String, IBinder>());
    }


    public static Map<String, IBinder> GetAllServices() {
        final HashMap<String, IBinder> maps = new HashMap<>();
        String[] list = ListServices();
        if (list != null) {
            for (String name : list) {
                maps.put(name, GetService(name));
            }
        }
        return maps;
    }

    public static String[] ListServices() {
        return listServices.call(null);
    }

    public final static IBinder GetService(String name) {
        IBinder service = sCache.get(name);
        if (service == null) {
            service = getService.call(null, name);
        }
        return service;
    }

    private static void SetService(Context context, String name, IBinder binder) {
        sCache.put(name, binder);
        RESET.Reset(name, context);
    }

    public static <T extends IInterfaceObserver> T GetObserver(String serviceName) {
        IBinder binder = sCache.get(serviceName);
        if (binder != null && binder instanceof IInterfaceObserver) {
            return (T) binder;
        }
        return null;
    }

    /**
     * 注册服务观察者
     *
     * @param observer
     * @return 成功返回之前设置的代理; 不成功返回自身对象
     */
    public static boolean registerObserver(Context context, String serviceName, IInterfaceObserver observer) {
        if (observer == null || serviceName == null) {
            return false;
        }
        IBinder binder = GetService(serviceName);
        if (binder == null) {
            iLog.l("Service: " + serviceName + " does not exist!");
            return false;
        }
        if (binder instanceof IInterfaceObserver) {
            iLog.l("Service: " + serviceName + " already register!");
            return false;
        }

        observer.setName(serviceName);
        AIDL aidl = AIDL.GetAIDL(serviceName, binder);
        if (aidl != null) {
            aidl.addClassLoader(AIDL.class.getClassLoader());
            observer.attach(aidl);
            IInterface srcObj = RESET.Update(serviceName, aidl);
            IInterface aidlObj = aidl.newStub$Proxy(binder, srcObj);
            if (aidlObj != null) {
                observer.setSource(aidlObj);
                observer.makeProxy(aidl.getAidlClass());
                SetService(context, serviceName, observer);
                return true;
            } else {
                iLog.l("Service: " + serviceName + "(" + binder + ") aidlObj is null!");
            }
        } else {
            iLog.l("Service: " + serviceName + "(" + binder + ") aidl is null!");
        }
        return false;
    }

    public static IInterfaceObserver unregisterObserver(Context context, String serviceName) {
        IInterfaceObserver observer = null;
        if (serviceName != null) {
            IBinder binder = GetService(serviceName);
            if (binder instanceof IInterfaceObserver) {
                observer = (IInterfaceObserver) binder;
                SetService(context, serviceName, observer.asBinder());
            }
        }
        return observer;
    }


    public static void LookAllService(Context context) {
        String[] list = ListServices();
        for (String name : list) {
            registerObserver(context, name, new IInterfaceObserver() {
                @Override
                protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
                    iLog.e(getName() + ".onInvoke: " + DEBUG.dumpCall(method, args, null, true));
                    return super.onInvoke(source, method, args);
                }
            });
        }
    }
}
