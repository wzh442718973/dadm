package com.glaring.colourful.bully.fs.lib.gms;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.fs.ak.AdKey;
import com.glaring.colourful.bully.fs.lib.FusionPack;
import com.glaring.colourful.bully.ad.DEBUG;
import com.glaring.colourful.bully.mo.AIDL;
import com.glaring.colourful.bully.mo.IInterfaceObserver;

public class C2dmREGISTER extends IInterfaceObserver.IConnection {
    public static final String ACTION = "com.google.android.c2dm.intent.REGISTER";

    //android.os.IMessenger

    @Override
    public void onConnectedBind(AIDL aidl, ComponentName name, IBinder service) {
        super.onConnectedBind(aidl, name, service);
    }

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        if(AdKey.DBG_LOG)Log.e(AdKey.TAG, getClass().getSimpleName() + ": " + DEBUG.dumpCall(method, args, null, true));
        final String name = method.getName();
        if("send".equals(name)){
            Message msg = (Message) args[0];
            Bundle bundle = msg.peekData();
            if(bundle != null){
                bundle.putString("pkg", FusionPack.A.getPkgName());
            }
        }
        return super.onInvoke(source, method, args);
    }

}
