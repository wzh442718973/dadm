package com.glaring.colourful.bully.games.lib;

import android.util.Log;

import java.lang.reflect.Method;

import com.glaring.colourful.bully.games.ak.AdKey;
import com.glaring.colourful.bully.base.DEBUG;
import com.glaring.colourful.bully.supers.IInterfaceObserver;

class UsageStatsObserver extends IInterfaceObserver {

    public static int indexOfClass(Object[] args, int[] indexs, Class clszz) {
        int num = 0;
        if (args != null && clszz != null) {
            for (int i = 0; i < args.length; ++i) {
                if (args[i] != null && clszz.isInstance(args[i])) {
                    indexs[num] = i;
                    num++;
                }
            }
        }
        return num;
    }

    @Override
    protected Object onInvoke(Object source, Method method, Object[] args) throws Throwable {
        int indexs[] = new int[args.length];
        int num = indexOfClass(args, indexs, String.class);
        for (int i = 0; i < num; ++i) {
            FusionPack pack = AAAHelper.findPack((String) args[indexs[i]], false);
            if (pack != null) {
                args[indexs[i]] = FusionPack.A.getPkgName();
            }
        }
        if (AdKey.DBG_LOG)
            Log.e(AdKey.TAG, "UsageStats.callInvoke: " + DEBUG.dumpCall(method, args, null, true));
        return super.onInvoke(source, method, args);
    }
}
