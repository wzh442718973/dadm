package com.glaring.colourful.bully.fs.lib.gms;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.ad.DEBUG;
import com.glaring.colourful.bully.mo.AIDL;
import com.glaring.colourful.bully.mo.IInterfaceObserver;

public class AdvertisingIdService extends IInterfaceObserver.IConnection {

    public static final String ACTION = "com.google.android.gms.ads.identifier.service.START";


    @Override
    public void onConnectedBind(AIDL aidl, ComponentName name, IBinder service) {
        aidl.setAidlClass("com.google.android.gms.internal.ads_identifier.zzf");
        aidl.setStubClass("com.google.android.gms.internal.ads_identifier.zze");

        super.onConnectedBind(aidl, name, service);
    }

    @Override
    public boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        return super.transact(code, data, reply, flags);
    }

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        if(AdKey.DBG_LOG)Log.e(AdKey.TAG, getClass().getSimpleName() + ": " + DEBUG.dumpCall(method, args, null, true));
        Object result = super.onInvoke(source, method, args);
        return result;
    }
}
